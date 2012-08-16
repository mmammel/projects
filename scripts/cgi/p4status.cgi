#!/usr/local/bin/perl -w

use CGI;

open (PS, "/bin/ps -U p4 |") || &error ("Couldn't call ps");

#$logFile = "/home/p4/logs/" . &isoDate();
$logFile = "/usr/local/log/p4err";

$Q = new CGI;

print $Q->header();
print $Q->start_html(-title => 'P4 Status');
print "<TABLE>\n";
print $Q->TR(
    $Q->th('Process'),
    $Q->th('CPU Time'),
    $Q->th('Start Time'),
    $Q->th('User'),
    $Q->th('Command')
    ), "\n";

while(<PS>) {
    #print STDERR "PS: $_"; 
    if(/^\s*(\d+)\s*\S*\s+([\d:\.]+)\s*p4d$/) {
        ($pid, $cpuTime) = ($1, $2);
        $line = qx?grep "pid $pid " $logFile | /usr/bin/tail -1?;
        #print "line: $line<BR>\n";
        if($line =~ /^\s*[\d\/]+\s+(\d+):(\d+):(\d+)\s+pid\s+\d+\s+([-\w@]+)\s+[\d\.:]+\s+\[.*\]\s+'(.*)'/) {
            ($hour, $minute, $second, $user, $command) = 
                ($1, $2, $3, $4, $5); 

            print "<TR>\n";
            print "<TD>$pid</TD>\n";
            print "<TD>$cpuTime</TD>\n";
            printf "<TD>%02d:%02d:%02d</TD>\n", $hour, $minute, $second;
            print "<TD>$user</TD>\n";
            print "<TD>$command</TD>\n";
            print "</TR>";
       } else {
            print "<TR>\n";
            print "<TD>$pid</TD>\n";
            print "<TD>$cpuTime</TD>\n";
            print "<TD COLSPAN=\"3\"><I>No log entry found.</I></TD>\n";
            print "</TR>";
       }
    }
}
close PS;
print "</TABLE>";
print $Q->end_html(), "\n";


sub error {
    my($msg) = @_;

    my $Q = new CGI;
    print $Q->header();
    print $Q->start_html(-title => 'Error');
    print $Q->h2($msg);
    print $Q->end_html();
    exit 0;
}

sub isoDate {
    my($sec, $min, $hour, $mday, $mon, $year) = localtime;

    sprintf "%04d-%02d-%02d", $year+1900, $mon+1, $mday;
}

sub isoTime {
    my($sec, $min, $hour) = localtime;

    sprintf "%02d:%02d:%02d", $hour, $min, $sec;
}
