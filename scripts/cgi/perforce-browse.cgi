#!d:/cygwin/bin/perl
#
# perfbrowse.perl -- CGI browser for PERFORCE
#
# $Id: //info.geodesic.com/perforce-browse.cgi#50 $
#
# Point P4PORT and P4CLIENT differently if you're not using the defaults.
# PATH is set to pick up the Perforce client program.
#
# Updated by Jeff Marshall at Paragon Software, Inc.
# (jam@paragon-software.com)
#
# Fixed for 99.1 by Rob Chandhok <chandhok@within.com>
#   - Anywhere the program was looking for "Date:", I made it also accept
#     headers like "Access:" and "Update:"
#
# Please email me your improvements
#
# My changes:
#   - Added white background
#   - New browsing goodies -- like:
#     o clients
#     o users
#     o opened files, by client and user
#     o branches
#     o labels
#     o jobs
#     o files within a given client
#   - Footer on every page with a link to the "top browser page" and links
#     to the browsing functionality listed above
#   - Also put in hyperlinks anywhere a perforce user, client or mail address
#     is seen.
#
# Tested with p4d 98.2 on a solaris 2.6 sparc server.
#
# Note: I'm not a perl programmer...

$Depot = "projects";

$Debug = 1;

$ENV{P4PORT} = "127.0.0.1:1666";
$ENV{P4USER} = "mmammel";
$ENV{P4CLIENT} = "MJM-PROJECTS";
$ENV{P4PASSWD} = "*.perforce";
#$ENV{PATH} .= ";d:\\program files\\perforce";

# Commented out and replaced with Geodesic settings.
# rb 1999-06-14
#$ENV{P4PORT} = "YYYY";
#$ENV{P4CLIENT} = "p4browse";
#$ENV{P4USER} = "XXXXX";
#$ENV{PATH} .= ":/usr/local/bin";
#$ENV{CODELINES} = "projects.list";

# Boilerplate

$localname = "http://127.0.0.1/cgi-bin/perforce-browse.cgi";

$BLUE = qq(<font color="#0000FF">);
$GREEN = qq(<font color="#006600">);
$RED = qq(<font color="#990000">);
$END = qq(</font>);

$ADD = $BLUE;
$ADDEND = $END;
$DEL = "<STRIKE>$RED";
$DELEND = "$END</STRIKE>";
$WS = $GREEN;
$WSEND = $END;

$MAXCONTEXT = 30;
$NCONTEXT = 10;

@HTMLHEADER = (
    "Content-type: text/html\n",
    "\n",
    "<html>\n",
    "<head>\n",
    "<body bgcolor=\"#ffffff\">\n" );

@HTMLERROR = (
    "Content-type: text/html\n",
    "\n",
    "<html>\n",
    "<head>\n",
    "<body bgcolor=\"#ffffff\">\n" );

@DIFFHEADER = ("<center><pre>",
        "$ADD added lines $ADDEND\n",
        "$DEL deleted lines $DELEND\n",
        "$WS lines with whitespace-only changes $WSEND\n",
        "</pre></center>\n");

@OTHER_FOOTERS = ();
#
# Switch on ARGV[0]
#

# handle isindex compatibility

if( @ARGV && $ARGV[0] !~ m!^@! ) {
    if($ARGV[0] !~ m{^/}) {
        $ARGV[0] = "//$Depot/$ARGV[0]";
    }
    unshift( @ARGV, "\@changes" ) 
}

################################
#
# No arguments. 
#
#   Put up the introductory screen.
#
################################

#if (!@ARGV) {
#    print @HTMLHEADER,
#   "<title>Perforce Browser</title>\n",
#   "<center><h1>Perforce Browser</h1>\n</center>",
#   "<i>This browser allows you to view information about your",
#       " Perforce server.\n",
#   "The first step is to select what you want to view</i>\n",
#   "<hr>";
#
#    print "<li>", &url( "\@browse", "Browse Depots"), "\n";
#    print "<li>", &url( "\@users", "Browse Users"), "\n";
#    print "<li>", &url( "\@clients", "Browse Clients"), "\n";
#    print "<li>", &url( "\@jobs", "Browse Jobs"), "\n";
#}
if ( !@ARGV) {

    if ($ENV{CODELINES}) {
    open( P4, "$ENV{CODELINES}" ) || &bail( "No codelines file." );
    @CODELINES = <P4>;
    }
    else
    {
    # Default codelines data is just a simple list of everything.
    # If $CODELINES is set in the environment, the codelines comes
    # from that.  The format is:
    #
    #   description1
    #   //path1
    #   description2
    #   //path2

    @CODELINES = (
          "All changelists to the projects depot\n",
          "//projects/...\n",
          "MapConverter changelists\n",
          "//projects/java/MapConverter/...\n",
          "X-Link Converter changelists\n",
          "//projects/java/XLink/...\n",
          "TrackOne changelists\n",
          "//projects/TrackOne/...\n",
          "TraxaMED changelists\n",
          "//projects/TraxaMED/...\n");
    }

    print 
    @HTMLHEADER,
    "<title>Perforce Changelist Browser</title>\n",
    "<center><h1>Perforce Changelist Browser</h1>\n</center>",
    "<i>This browser allows you to view the history of a Perforce depot.\n",
    "The first step is to select the files you want history for.</i>\n",
    "<isindex prompt=\"Click below or type a file pattern here: \">\n",
    "('//$Depot/' is the default.)\n";

    foreach ( @CODELINES )
    {
    chop;

    if( m:^/: )
    {
        print "<li>", &url( "\@changes+$_", $_ ), "\n";
    }
    elsif( !/^\s*$/ )
    {
        print "</blockquote><p><h3>$_</h3><blockquote>\n";
    }
    }
}
elsif ( $ARGV[0] eq "\@users" ) {
    &p4open( 'P4', "users|" );

    print @HTMLHEADER,
    "<title>Perforce Users</title>\n",
    "<center><h1>Perforce Users</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " Perforce users.\n",
    "The first step is to select which user you want to view</i>\n",
    "<hr>";

    print
    "<table cellpadding=1>",
    "<tr align=left><th>User<th>Email<th>Full Name",
    "<th>Last Accessed</tr>\n";

    # Sample:
    # jam <jam@bar.com> (Jeffrey A. Marshall) accessed 1998/07/03

    while( <P4> ) {
#   print "<tr><td>$_</tr>\n";
    if (local( $user, $email, $fullname, $accessed ) = 
        /(\S+) <(\S+)> \((.*)\) accessed (\S+)/)
    {
        print "<tr>",
              "<td valign=top>", &url ( "\@user+$user", "$user"),
          "<td valign=top>", &mailto ( "$email" ),
              "<td valign=top>", "$fullname",
          "<td valign=top>", "$accessed",
              "</tr>\n";
    }
    }
    print "</table>\n";
    close P4;
} elsif ( $ARGV[0] eq "\@user" ) {
    local( $user, $email, $access, $fullname, $jobview );
    $user = $ARGV[1];
    &p4open ('P4', "user -o $user|");

    while (<P4>)
    {
    next if (/^User:/);
    next if (/^Email:/ && (( $email ) = /^Email:\s*(.*)$/ ));
    next if (/^Access:/ && (( $access ) = /^Access:\s*(.*)$/ ));
    next if (/^FullName:/ && (( $fullname ) = /^FullName:\s*(.*)$/ ));
    next if (/^JobView:/ && (( $jobview ) = /^JobView:\s*(.*)$/ ));
    last if (/^Reviews:/);
    }

    print @HTMLHEADER,
        "<title>Perforce User Information for $user</title>\n",
    "<center><h1>Perforce User Information for $user</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " a given Perforce user.</i>\n<hr>",
    "<h3>Full Name</h3><pre>        $fullname</pre>\n",
    "<h3>Email</h3><pre>        ", &mailto ("$email"), "</pre>\n",
    "<h3>Last Access</h3><pre>        $access</pre>\n",
    "<h3>JobView</h3><pre>        $jobview</pre>\n",
    "<h3>Reviews</h3>\n<pre>";

    while (<P4>)
    {
       print;
    }
    print "</pre>\n";
    close P4;
    @OTHER_FOOTERS = (" | ",
              , &url ("\@opened+user+$user",
                  "Files Opened by $user"));
} elsif ( $ARGV[0] eq "\@clients" ) {
    &p4open( 'P4', "clients|" );

    print @HTMLHEADER,
    "<title>Perforce Clients</title>\n",
    "<center><h1>Perforce Clients</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " Perforce clients.\n",
    "The first step is to select which client you want to view</i>\n",
    "<hr>";

    print
    "<table cellpadding=1>",
    "<tr align=left><th>Client<th>Date<th>Root Directory",
    "<th>Description</tr>\n";

    # Sample:
    # Client oak.template 1998/06/25 root /tmp 'OAK client template '

    while( <P4> ) {
    if (local( $client, $date, $root, $descrip ) = 
        /Client (\S+) (\S+) root (\S+) '(.*)'/)
    {
        print "<tr>",
              "<td valign=top>", &url ( "\@client+$client", "$client"),
          "<td valign=top>$date",
          "<td valign=top>$root",
          "<td valign=top>$descrip",
              "</tr>\n"
    }
    }
    print "</table>\n";
    close P4;
} elsif ( $ARGV[0] eq "\@client" ) {
    local( $client, $date, $owner, $root, $opts );
    $client = $ARGV[1];
    &p4open ('P4', "client -o $client|");

    while (<P4>)
    {
    next if (/^Client:/);
    next if (/^Date:/ && (( $date ) = /^Date:\s*(.*)$/ ));
    next if (/^Access:/ && (( $date ) = /^Access:\s*(.*)$/ ));
    next if (/^Owner:/ && (( $owner ) = /^Owner:\s*(\S+)$/ ));
    last if (/^Description:/);
    }

    print @HTMLHEADER,
        "<title>Perforce Client Information for $client</title>\n",
    "<center><h1>Perforce Client Information for $client</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " a given Perforce client.</i>\n<hr>";

    if ("$date" eq "") {
    print "<h3>Client <i>$client</i> doesn't exist</h3>";
    }
    else
    {
    print "<h3>Date</h3><pre>        $date</pre>\n",
          "<h3>User</h3><pre>        ",
          &url ("\@user+$owner", "$owner"), "</pre>\n",
          "<h3>Description</h3><pre>\n";

    while (<P4>)
    {
       next if (/^$/);
       last if (/^Root:/ && (( $root ) = /^Root:\s*(.*)$/ ));
       print;
    }
    while (<P4>)
    {
       next if (/^Options:/ && (( $opts ) = /^Options:\s*(.*)$/ ));
       last if (/^View:/);
        }
    print "</pre><h3>Root Directory</h3><pre>        $root</pre>\n",
          "<h3>Options</h3><pre>        $opts</pre>\n",
      "<h3>View</h3><pre>";
    while (<P4>)
    {
        last if (/^$/);
        print;
    }
    print "</pre>";
    }
    close P4;
    @OTHER_FOOTERS = (" | ", &url ("\@files+\@$client", "Files in $client"),
              " | ", &url ("\@opened+client+$client", "Files Opened in $client"));
} elsif ( $ARGV[0] eq "\@jobs" ) {
    &p4open( 'P4', "jobs|" );

    print @HTMLHEADER,
    "<title>Perforce Jobs</title>\n",
    "<center><h1>Perforce Jobs</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " Perforce Jobs.\n",
    "The first step is to select which job you want to view</i>\n",
    "<hr>";

    print
    "<table cellpadding=1>",
    "<tr align=left><th>Job Name<th>Date<th>User",
    "<th>Status<th>Description</tr>\n";

    # Sample:
    # job000011 on 1998/07/03 by jam *open* 'Another test. '

    while( <P4> ) {
    if (local( $name, $date, $user, $status, $descrip ) = 
        /(\S+) on (\S+) by (\S+) \*(\S+)\* '(.*) '/)
    {
        print "<tr>",
              "<td valign=top>", &url ( "\@job+$name", "$name"),
          "<td valign=top>$date",
              "<td valign=top>", &url ( "\@user+$user", "$user"),
          "<td valign=top>$status",
          "<td valign=top>$descrip",
              "</tr>\n";
    }
    }
    print "</table>\n";
    close P4;
} elsif ( $ARGV[0] eq "\@branches" || $ARGV[0] eq "\@labels" ) {
    local ($type, $cmd, $viewer);

    if ($ARGV[0] eq "\@branches")
      {
    $type = "Branch";
    $cmd = "branches";
    $viewer = "branch";
      }
    else
      {
    $type = "Label";
    $cmd = "labels";
    $viewer = "label";
      }
    &p4open( 'P4', "$cmd|" );

    print @HTMLHEADER,
    "<title>Perforce ${type}es</title>\n",
    "<center><h1>Perforce ${type}es</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " Perforce ${type}es.\n",
    "The first step is to select which $viewer you want to view</i>\n",
    "<hr>",
    "<table cellpadding=1>",
    "<tr align=left><th>$type Name<th>Date<th>Description</tr>\n";

    # Sample:
    # Branch test 1998/07/03 'Created by jam. '
    # Label example-label.template 1998/06/26 'Label tempalte for the example '

    while( <P4> ) {
    if (local( $name, $date, $descrip ) = 
        /$type (\S+) (\S+) '(.*) '/)
    {
        print "<tr>",
              "<td valign=top>", &url ( "\@$viewer+$name", "$name"),
          "<td valign=top>$date",
              "<td valign=top>$descrip",
              "</tr>\n";
    }
    }
    print "</table>\n";
    close P4;
} elsif ( $ARGV[0] eq "\@branch" || $ARGV[0] eq "\@label") {
    local( $type, $cmd, $name, $date, $owner, $opts, $filespec );

    $name = $ARGV[1];

    if ($ARGV[0] eq "\@branch")
      {
        $type = "Branch";
        $cmd = "branch";
    $filespec = "//$name/...";
      }
    else
      {
        $type = "Label";
    $cmd = "label";
    $filespec = "+\@$name";
      }

    &p4open ('P4', "$cmd -o $name|");

    while (<P4>)
    {
    next if (/^$type:/);
    next if (/^Date:/ && (( $date ) = /^Date:\s*(.*)$/ ));
    next if (/^Access:/ && (( $date ) = /^Access:\s*(.*)$/ ));
    next if (/^Update:/ && (( $date ) = /^Update:\s*(.*)$/ ));
    next if (/^Owner:/ && (( $owner ) = /^Owner:\s*(\S+)$/ ));
    last if (/^Description:/);
    }

    print @HTMLHEADER,
        "<title>Perforce $type Information for $name</title>\n",
    "<center><h1>Perforce $type Information for $name</h1>\n</center>",
    "<i>This browser allows you to view information about ",
        " a given Perforce $cmd.</i>\n<hr>";

    if ("$date" eq "") {
    print "<h3>$type <i>$name</i> doesn't exist</h3>";
    }
    else
    {
    print "<h3>Date</h3><pre>        $date</pre>\n",
          "<h3>User</h3><pre>        ",
          &url ("\@user+$owner", "$owner"), "</pre>\n",
          "<h3>Description</h3><pre>\n";

    while (<P4>)
    {
       next if (/^$/);
       last if (/^View:/);
       last if (/^Options:/ && (( $opts ) = /Options:\s*(.*)/ ));
       print;
    }

    print "</pre>";

    if ("$opts" ne "")
      {
        print "<h3>Options</h3><pre>        $opts</pre>\n";
        while (<P4>)
         {
           last if (/^View:/);
         }
      }
    print "<h3>View</h3><pre>";
    $filespec = "" if ("$cmd" eq "branch");
    while (<P4>)
    {
        last if (/^$/);
        if ("$cmd" eq "branch")
          {
            local ($spec) = /^\s*\S+ (\S+)$/;
        $filespec = "$filespec+$spec";
          }
        print;
    }
    print "</pre>";
    }
    close P4;

    @OTHER_FOOTERS = (" | ", &url ("\@files$filespec", "Files in this $type"));
    if ($ARGV[0] eq "\@branch") {
        push @OTHER_FOOTERS,  
            (" | ", &url("\@diff2-q-b+$name", "Differences in this branch"));
    }

}
################################
#
# changes [ path ]
#
#   show changes for path
#
################################

elsif( $ARGV[0] eq "\@changes" ) {

    &p4open( 'P4', "changes -l \"$ARGV[1]\"|" );

    print 
    @HTMLHEADER,
    "<title>Changelists for $ARGV[1]</title>\n",
    "<center><h1>Changelists for $ARGV[1]</h1></center>\n",
    "<i>This form displays the changelists for the files you've selected.\n",
    "Click on the changelist number to see details of a change.  Changelists\n",
    "are listed in reverse chronological order, so you see what's\n",
    "most recent first.</i>\n",
    "<hr><dl>\n";

    while (<P4>) {

        s/&/&amp;/g;
        s/\"/&quot;/g;
        s/</&lt;/g;
        s/>/&gt;/g;

    if( local( $change, $on, $user, $client ) = /Change (\d+) on (\S+) by (\S+)@(\S+)/ ) 
    {
        print 
        "<dt>", &url( "\@describe+$change", "Changelist $change" ),
        " on $on by ", &url ("\@user+$user", "$user"),
        "\@", &url ("\@client+$client", "$client"), "<dd>\n";
    } 
    else 
    {
        chop;
        print "<tt>$_</tt><br>\n";
    }
    }

    print "</dl>\n";

    close P4;
}

################################
#
# describe change
#
#   describe a change
#
################################

elsif( $ARGV[0] eq "\@describe") {
    local @files = ();
    local @revs = ();
    local @actions = ();

    &p4open( 'P4', "describe -s \"$ARGV[1]\"|" );

    $_ = <P4>;

    ( local($chn, $user, $client, $date, $time) = 
    /Change (\d+) by (\S*)@(\S*) on (\S*) (\S*)/ )
    || &bail( $_ );

    print 
    @HTMLHEADER,
    "<title>Changelist $chn</title>\n",
    "<center><h1>Changelist $chn</h1></center>\n",
    "<i>This form displays the details of a changelist.  For each of the\n",
    "files affected, you can click on:\n",
    "<ul>\n",
    "<li>Filename -- to see the complete file history\n",
    "<li>Revision Number -- to see the file text\n",
    "<li>Action -- to see the deltas (diffs)\n",
    "</ul></i>",
    "<hr><pre>\n",
    "<strong>Author        </strong>", &url ("\@user+$user", "$user"), "\n",
    "<strong>Client        </strong>", &url ("\@client+$client", "$client"), "\n",
    "<strong>Date          </strong>$time $date\n",
    "</pre><hr>\n",
    "<h2>Description</h2>\n",
    "<pre>\n";

    while(<P4>) {
    next if /^\s*$/;
    last if /^Jobs fixed/;
    last if /^Affected files/;
    print $_;
    }

    print
    "</pre>",
    "<hr>\n";

    # display jobs

    if( /^Jobs fixed/ )
    {
    print 
        "<h2>Jobs Fixed</h2>\n",
        "<ul>\n";

    while ( <P4> ) {
        local( $job, $time, $user, $client );

        while( ( $job, $time, $user, $client ) = 
        /(\S*) fixed on (\S*) by (\S*)@(\S*)/ )
        {
        print
            "<li><h3>",
            &url( "\@job+$job", $job ),
            "</h3><pre>\n";

        while(<P4>) {
            last if /^\S/;
            print $_;
        }

        }

        print "</pre>\n";

        last if /^Affected files/;
    }

    print 
        "</dl>",
        "<hr>\n";
    }

    print
    "<h2>Files</h2>\n",
    "<ul>\n",
    "<table cellpadding=1>",
    "<tr align=left><th>File<th>Rev<th>Action</tr>\n";

    # Sample:
    # ... //depot/main/p4/Jamrules#71 edit

    while(<P4>) {

    if( local( $file, $rev, $act ) = /\.\.\. (.+?)#(\d*) (\S*)/ )
    {
        push @files, $file;
        push @revs, $rev;
        push @actions, $act;
        print 
        "<tr>",
        "<td valign=top>", &url( "\@filelog+$file", "$file" ),
        "<td valign=top>", &url( "\@print+$file+$rev", "$rev" ),
        "<td valign=top>", &url( "\@diff+$file+$rev+$act", "$act" ),
        "</tr>\n";
    }
    }
    print
    "</table></ul>\n";

    close P4;
    print "<hr><center><h1>Changes</h1></center>",
           @DIFFHEADER;

    for($i = 0; $i <= $#files; $i++) {
      local($file, $rev, $action) = ($files[$i], $revs[$i], $actions[$i]);
      print "<hr/>";
      print "<h2>$file</h2>";
      print "<TABLE><TR>";
      if($action eq "edit" or $action eq "delete" or $action eq "integrate") {
        print "<TD>", &url("\@print+$file+$rev-1", "Original"), "</TD>";
      }
      if($action eq "edit" or $action eq "add" or $action eq "integrate" or $action eq "branch") {
        print "<TD>", &url("\@print+$file+$rev", "Modified"), "</TD>";
      }
      print "</TR></TABLE><br>";
      if($action eq "edit" or $action eq "integrate") {
        &diff2_inner($file, $rev - 1, $file, $rev);
      }
      if($action eq "add") {
        print "$ADD";
        &printfile_inner($file, $rev);
        print "$ADDEND";
      }
    }
  } 
elsif ($ARGV[0] eq "\@describe-v") {
  my (@files, @revs, @acts);
    &p4open( 'P4', "describe -s \"$ARGV[1]\"|" );

    $_ = <P4>;

    ( local($chn, $user, $client, $date, $time) = 
    /Change (\d+) by (\S*)@(\S*) on (\S*) (\S*)/ )
    || &bail( $_ );

    print 
    @HTMLHEADER,
    "<title>Changelist $chn</title>\n",
    "<center><h1>Changelist $chn</h1></center>\n",
    "<i>This form displays the details of a changelist.  For each of the\n",
    "files affected, you can click on:\n",
    "<ul>\n",
    "<li>Filename -- to see the complete file history\n",
    "<li>Revision Number -- to see the file text\n",
    "<li>Action -- to see the deltas (diffs)\n",
    "</ul></i>",
    "<hr><pre>\n",
    "<strong>Author        </strong>", &url ("\@user+$user", "$user"), "\n",
    "<strong>Client        </strong>", &url ("\@client+$client", "$client"), "\n",
    "<strong>Date          </strong>$time $date\n",
    "</pre><hr>\n",
    "<h2>Description</h2>\n",
    "<pre>\n";

    while(<P4>) {
    next if /^\s*$/;
    last if /^Jobs fixed/;
    last if /^Affected files/;
    print $_;
    }

    print
    "</pre>",
    "<hr>\n";
    while(<P4>) {
      if( local( $file, $rev, $act ) = /\.\.\. (.+?)#(\d*) (\S*)/ ) {
        push @files, $file;
        push @revs, $rev;
        push @acts, $act;
      }
    }
    close P4;
    for($i = 0; $i <= $#files; $i++) {
        local ($file, $rev, $act) = ($files[$i], $revs[$i], $acts[$i]);
        print "<H2>", &url( "\@filelog+$file", "$file" ), "</H2>";
        print "<B>$act</B><BR/>";
        if($rev > 1 and ($act eq "edit" or $act eq "integrate")) {
            &diff2($file, $rev - 1, $file, $rev);
        }
    }
    print
  }
################################
#
# filelog file 
#
#   show filelog of the file
#
################################

elsif ($ARGV[0] eq "\@filelog") {

    local( $name ) = $ARGV[1];

    &p4open( 'P4', "filelog \"$name\"|" );

    $name = <P4>;
    chop $name;

    print 
    @HTMLHEADER,
    "<title>Filelog $name</title>\n",
    "<center><h1>Filelog $name</h1></center>\n",
    "<i>This form shows the history of an individual file across\n",
    "changes.  You can click on the following:\n",
    "<ul>\n",
    "<li>Revision Number -- to see the file text\n",
    "<li>Action -- to see the deltas (diffs)\n",
    "<li>Changelist -- to see the complete change description, including\n",
    "other files.\n",
    "</ul></i>",
    "<hr>\n";

    print
    "<table cellpadding=1>",
    "<tr align=left><th>Rev<th>Action<th>Date",
    "<th>User\@client<th>Changelist<th>Desc</tr>\n";

    # Sample:
    # ... #78 change 1477 edit on 04/18/1996 by user@client 'Fix NT mkdi'

    while( <P4> ) {
    if (local( $rev, $change, $act, $date, $user, $client, $edit_type, $desc ) =
        /\.\.\. \#(\d+) \S+ (\d+) (\S+) on (\S+) by (\S*)@(\S*) \((\S+)\) '(.*)'/)
    {
        if ($act eq 'branch') {
        $_ = <P4>;
        local ($fromname,$fromrev) = /.*branch from (\S+?)\#(\d+).*/;
        print
            "<tr>",
            "<td valign=top>", &url( "\@print+$name+$rev", "$rev" ),
            "<td valign=top>", &url( "\@filelog+$fromname+$fromrev", $act ),
            "<td valign=top>$date",
            "<td valign=top>", &url ("\@user+$user", "$user"), "\@",
                    &url ("\@client+$client", "$client"),
            "<td valign=top>", &url( "\@describe+$change", "$change" ),
            "<td valign=top><tt>$desc</tt>",
            "</tr>\n";
        }
        elsif ($act eq 'delete') {
        print
            "<tr>",
            "<td valign=top>", &url( "\@print+$name+$rev", "$rev" ),
            "<td valign=top>$DEL$act$DELEND",
            "<td valign=top>$date",
            "<td valign=top>", &url ("\@user+$user", "$user"), "\@",
                    &url ("\@client+$client", "$client"),
            "<td valign=top>", &url( "\@describe+$change", "$change" ),
            "<td valign=top><tt>$desc</tt>",
            "</tr>\n";
        }
        else {
        print
            "<tr>",
            "<td valign=top>", &url( "\@print+$name+$rev", "$rev" ),
            "<td valign=top>", &url( "\@diff+$name+$rev+$act", $act ),
            "<td valign=top>$date",
            "<td valign=top>", &url ("\@user+$user", "$user"), "\@",
                    &url ("\@client+$client", "$client"),
            "<td valign=top>", &url( "\@describe+$change", "$change" ),
            "<td valign=top><tt>$desc</tt>",
            "</tr>\n";
        }
    }
    }

    print "</table>\n";

    close P4;
}
elsif ($ARGV[0] eq "\@files") {
    &p4open( 'P4', "files @ARGV[1..$#ARGV]|" );
    print 
    @HTMLHEADER,
    "<title>Files for $ARGV[1..$#ARGV]</title>\n",
    "<center><h1>Files for @ARGV[1..$#ARGV]</h1></center>\n",
    "<i>This form displays files in the depot for a given revision.\n",
    "For each of the files, you can click on:\n",
    "<ul>\n",
    "<li>Filename -- to see the complete file history\n",
    "<li>Revision Number -- to see the file text\n",
    "<li>Action -- to see the deltas (diffs)\n",
    "<li>Changelist -- to see the complete change description, including\n",
    "other files.\n",
    "</ul></i>",
    "<hr>\n";

    print
    "<h3>Files</h3>\n",
    "<ul>\n",
    "<table cellpadding=1>",
    "<tr align=left><th>File<th>Rev<th>Action<th>Changelist</tr>\n";

    # Sample:
    # //example/find/TypeExpr.java#1 - add change 5 (ktext)

    while(<P4>) {
    if( local( $file, $rev, $act, $change, $type ) =
        /(\S+)#(\d*) - (\S+) change (\d*) \((\S+)\)/ )
    {
        print 
        "<tr>",
        "<td valign=top>", &url( "\@filelog+$file", "$file" ),
        "<td valign=top>", &url( "\@print+$file+$rev", "$rev" ),
        "<td valign=top>", &url( "\@diff+$file+$rev+$act", "$act" ),
        "<td valign=top>", &url( "\@describe+$change", "$change" ),
#       "<td valign=top>", "$type",
        "</tr>\n";
    }
    }
    print
    "</table></ul>\n";

    close P4;
}
elsif ($ARGV[0] eq "\@opened") {
    &p4open( 'P4', "opened -a|" );

    print 
    @HTMLHEADER,
    "<title>Opened files for @ARGV[1..$#ARGV]</title>\n",
    "<center><h1>Opened files for @ARGV[1..$#ARGV]</h1></center>\n",
    "<i>This form displays files opened by the specified $ARGV[1].\n",
    "For each of the files, you can click on:\n",
    "<ul>\n",
    "<li>Filename -- to see the complete file history\n",
    "<li>Revision Number -- to see the file text\n",
    "<li>User -- to see the a user description\n",
    "<li>Client -- to see the a client description\n",
    "</ul></i>",
    "<hr>\n";

    print
    "<h3>Files</h3>\n",
    "<ul>\n",
    "<table cellpadding=1>",
    "<tr align=left><th>File<th>Rev<th>Action<th>Change List",
    "<th>Type<th>User\@Client</tr>\n";

    # Sample:
    # //foo/file.java#2 - edit default change (text) by user@client

    while(<P4>) {
    if (local( $file, $rev, $act, $change, $type, $user, $client ) =
         /(\S+)#(\d*) - (\S+) (\S+) change \((\S+)\) by (\S+)@(\S+)/)
      {
        next if (($ARGV[1] eq "user" ? $user : $client) ne $ARGV[2]);
        print 
        "<tr>",
        "<td valign=top>", &url( "\@filelog+$file", "$file" ),
        "<td valign=top>", &url( "\@print+$file+$rev", "$rev" ),
        "<td valign=top>$act<td valign=top>$change<td valign=top>$type",
        "<td valign=top>", &url( "\@user+$user", "$user" ), "\@",
        &url( "\@client+$client", "$client" ),
        "</tr>\n";
    }
    }
    print
    "</table></ul>\n";

    close P4;
}
################################
#
# print file rev action
#
#   print file text
#
################################

elsif ($ARGV[0] eq "\@print" || $ARGV[0] eq "\@list") {

    &printfile( @ARGV[1..3]);
}

################################
#
# diff file rev action
#
#   describe a change
#
################################

elsif ($ARGV[0] eq "\@diff") {

    local( $name, $rev, $mode ) = @ARGV[1..3];
    local( $nchunk ) = 0;

    print
    @HTMLHEADER,
    "<title>$name#$rev</title>\n",
    "<center><h1>$name#$rev - $mode</h1></center>\n",
    "<i>This form shows you the deltas (diffs) that lead from the\n",
    "previous to the current revision.</i>\n",
    "<hr>\n";

    if ($mode ne 'add' && $mode ne 'delete' && $mode ne 'branch') {
        &diff2($name, $rev - 1, $name, $rev);
    } else {
        print "<P>Add, delete, or branch</P>\n";
    }
}


################################
#
# job job
#
#   describe a job
#
################################

elsif ($ARGV[0] eq "\@job") {

    local( $user, $job, $status, $time, $date );

    &p4open( 'P4', "job -o \"$ARGV[1]\"|" );

    while( <P4> )
    {
    next if ( /^Job/ && ( ( $job ) = /Job:\s(\S*)/ ) );
    next if ( /^User/ && ( ( $user ) = /User:\s*(\S*)/ ) );
    next if ( /^Status/ && ( ( $status ) = /Status:\s*(\S*)/ ) );
    next if ( /^Date/ && ( ( $date, $time ) = /Date:\s*(\S*) (\S*)/ ) );
    next if ( /^Access/ && ( ( $date, $time ) = /Access:\s*(\S*) (\S*)/ ) );
    last if ( /^Description/ );
    }

    print 
    @HTMLHEADER,
    "<title>Job $job</title>\n",
    "<center><h1>Job $job</h1></center>\n",
    "<i>This form displays the details of a job.  You can click on a\n",
    "change number to see its description.\n",
    "</i>",
    "<hr><pre>\n",
        "<strong>User          </strong>", &url ("\@user+$user", "$user"), "\n",
    "<strong>Status        </strong>$status\n",
    "<strong>Date          </strong>$time $date\n",
    "</pre><hr>\n",
    "<h2>Description</h2>\n",
    "<pre>\n";

    while(<P4>) {
    print $_;
    }

    print
    "</pre>",
    "<hr>\n";

    close P4;

    # display fixes

    &p4open( 'P4', "fixes -j $ARGV[1]|" );

    $count = 0;

    while( <P4> )
    {
    print
        "<h2>Fixes</h2>\n",
        "<ul>\n",
        "<table cellpadding=1>",
        "<tr align=left><th>Changelist<th>Date<th>User\@Client</tr>\n"
        if( !$count++ );

    # jobx fixed by change N on 1997/04/25 by user@host

    if( local( $change, $date, $user, $client ) 
        = /\S* fixed by change (\S*) on (\S*) by (\S*)@(\S*)/ ) 
    {
        print 
        "<tr>",
        "<td valign=top>", &url( "\@describe+$change", "$change" ),
        "<td valign=top>", $date,
        "<td valign=top>", &url ("\@user+$user", "$user"), "\@",
            &url ("\@client+$client", "$client"),
        "</tr>\n";
    }
    }

    print "</table></ul>\n"
    if( $count );

    close P4;
}
elsif ($ARGV[0] eq "\@diff2-b") {
   local $branch = $ARGV[1];
    local ($sourcePoint, $targetPoint) = ($ARGV[2], $ARGV[3]);

    if(defined($sourcePoint)) {
        if(!defined($targetPoint)) {
      &p4open('P4', "counters |");
            while(<P4>) {
                if(/^change = (\d+)/) {
                    $targetPoint = $1;
                }
            }
            close P4;
        }
        &p4open( 'P4', "diff2 -b $branch \@$sourcePoint \@$targetPoint |" );
    } else {
        &p4open( 'P4', "diff2 -b $branch |" );
    }
    print @HTMLHEADER;
    print "<title>Diff2 of branch $branch</TITLE>\n";
    print "<h1 ALIGN=\"CENTER\">Diff2 of branch $branch</h1>\n";

    local @files1 = ();
    local @revs1 = ();
    local @files2 = ();
    local @revs2 = ();

    while(<P4>) {
        if(/==== < none > - (.*)#(.*) ====/) {
            push @files1, undef;
            push @revs1, undef;
            push @files2, $1;
            push @revs2, $2;
        } elsif(/==== (.*)#(.*) - <none> ===/) {
            push @files1, $1;
            push @revs1, $2;
            push @files2, undef;
            push @revs2, undef;
        } elsif(/==== (.*)#(.*) \(.*\) - (.*)#(.*) \(.*\) ====/) {
            push @files1, $1;
            push @revs1, $2;
            push @files2, $3;
            push @revs2, $4;
        }
    }
    close P4;
    for($i = 0; $i <= $#files1; $i++) {
      local($file1, $rev1, $file2, $rev2) =
        ($files1[$i], $revs1[$i], $files2[$i], $revs2[$i]);
      next unless (defined($file2) && $rev2 != 1);
      print "<h2>$file2</h2>";
      print "<TABLE><TR>";
      print "<TD>", &url("\@print+$file2+$rev1", "Original"), "</TD>",
            "<TD>", &url("\@print+$file2+$rev2", "Modified"), "</TD>",
            "</TR></TABLE><br>";
      &diff2($file2, 1, $file2, $rev2);
    }
}
elsif ($ARGV[0] eq "\@diff2-q-b") {
    local $branch = $ARGV[1];
    local ($sourcePoint, $targetPoint) = ($ARGV[2], $ARGV[3]);

    if(defined($sourcePoint)) {
        if(!defined($targetPoint)) {
      &p4open('P4', "counters |");
            while(<P4>) {
                if(/^change = (\d+)/) {
                    $targetPoint = $1;
                }
            }
            close P4;
        }
        &p4open( 'P4', "diff2 -b $branch \@$sourcePoint \@$targetPoint |" );
    } else {
        &p4open( 'P4', "diff2 -b $branch |" );
    }
    print @HTMLHEADER;
    print "<title>Diff2 of branch $branch</TITLE>\n";
    print "<h1 ALIGN=\"CENTER\">Diff2 of branch $branch</h1>\n";
    print "<P><I>This page shows the files that changed across a\n",
        "branch specification.  It shows the common stem between all trunk filespecs, and between all branch filespecs.\n</I></P>";
    print "<P><I>Select \"filelog\" for a trunk or branch file history.  Select the revision number to see the actual file.  Select \"trunk\", \"trunk-branch\", or \"branch\" to see the differences along the trunk from the branchpoint, across the branchspec, and along the branch from the branchpoint.\n</I></P>";

    local @files1 = ();
    local @revs1 = ();
    local @files2 = ();
    local @revs2 = ();
    local @trunk_matches_branch = ();

    while(<P4>) {
        next unless /====/;
        if(/==== < none > - (.*)#(.*) ====/) {
            push @files1, undef;
            push @revs1, undef;
            push @files2, $1;
            push @revs2, $2;
        } elsif(/==== (.*)#(.*) - <none> ===/) {
            push @files1, $1;
            push @revs1, $2;
            push @files2, undef;
            push @revs2, undef;
        } elsif(/==== (.*)#(.*) \(.*\) - (.*)#(.*) \(.*\) ====/) {
            push @files1, $1;
            push @revs1, $2;
            push @files2, $3;
            push @revs2, $4;
        }
        if(/identical/) {
          push @trunk_matches_branch, "true";
        } else {
          push @trunk_matches_branch, undef;
        }
    }
    close P4;


    local $lead1 = &lead(@files1);
    local $lead2 = &lead(@files2);
 
    if(defined($sourcePoint) && defined($targetPoint)) {
        print "<P><B>Trunk:</B> $lead1\@$sourcePoint<BR><B>Branch:</B> $lead2\@$targetPoint<BR>\n";
    } else {
        print "<P><B>Trunk:</B> $lead1<BR><B>Branch:</B> $lead2<BR>\n";
    }
    &p4open ('P4', "branch -o $branch|");
    print "<B>Description:</B><BR>";
    until (<P4> =~ /^Description:/ ) {}
    while (<P4>)
    {
       next if (/^$/);
       last if (/^View:/);
       last if (/^Options:/ && (( $opts ) = /^Options:\s*(.*)$/ ));
       print;
       print "<BR>";
    }
    print "</P>";
    print "<TABLE>\n";
    print "<TR><TH>File</TH> <TH>Trunk</TH> <TH>Rev</TH> <TH>Branch</TH> <TH>Rev</TH> <TH>Along Trunk</TH> <TH>Between Trunk and Branch</TH> <TH>Along Branch</TH>\n";

    local $i;
    local $len1 = length($lead1);
    local $len2 = length($lead2);

    for($i = 0; $i <= $#files1; $i++) {
        local($file1, $rev1, $file2, $rev2, $trunk_branch_match) =
            ($files1[$i], $revs1[$i], $files2[$i], $revs2[$i], $trunk_matches_branch[$i]);
        next unless (!defined($file2) || !defined($file1) || $rev2 != 1);
        print "<TR>\n";
        print STDERR ">>> $file1#$rev1 $file2#$rev2 $trunk_branch_match\n";
        if(!defined($file1)) {
            print "<TD>", substr($file2, $len2), "</TD>\n";
        } elsif(!defined($file2)) {
            print "<TD>", substr($file1, $len1), "</TD>\n";
        } elsif(substr($file2, $len2) eq substr($file1, $len1)) {
            print "<TD>", substr($file1, $len1), "</TD>\n";
        } else {
            print "<TD>", substr($file1, $len1), "<BR>",
                substr($file2, $len2), "</TD>\n";
        }

        if(defined($file1)) {
            print "<TD>", &url("\@filelog+$file1", "filelog"), "</TD>\n";
            print "<TD>", &url("\@print+$file1+$rev1", $rev1), "</TD>\n";
        } else {
            print "<TD COLSPAN=\"2\" ALIGN=\"CENTER\"><I>n/a</I></TD>\n";
        }

        if(defined($file2)) {
            print "<TD>", &url("\@filelog+$file2", "filelog"), "</TD>\n";
            print "<TD>", &url("\@print+$file2+$rev2", $rev2), "</TD>\n";
        } else {
            print "<TD COLSPAN=\"2\" ALIGN=\"CENTER\"><I>n/a</I></TD>\n";
        }

        if(defined($file1) && defined($file2)) {
            print "<TD>", &url("\@diff2+$file2+1+$file1+$rev1", "trunk"), "</TD>\n";
            if($trunk_branch_match) {
              print "<TD ALIGN=\"CENTER\"><I>n/a</I></TD>\n";
            } else {
              print "<TD>", &url("\@diff2+$file1+$rev1+$file2+$rev2", "trunk-branch"), "</TD>\n";
            }
        } else {
            print "<TD><I>n/a</I></TD>\n";
            print "<TD><I>n/a</I></TD>\n";
        }

        if(defined($file2) && $rev2 != 1) {
            print "<TD>", &url("\@diff2+$file2+1+$file2+$rev2", "branch"), "</TD>\n";
        } else {
            print "<TD><I>n/a</I></TD>\n";
        }
    
        print "</TR>\n";
    }

    print "</TABLE>\n";
}
elsif ($ARGV[0] eq "\@diff2") {
    local($file1, $rev1, $file2, $rev2) = @ARGV[1..4];
    print @HTMLHEADER;
    print "<title>Diff2 of $file1#$rev1 $file2#$rev2</TITLE>\n";
    print "<H1 ALIGN=\"CENTER\">Diff2 of $file1#$rev1 $file2#$rev2</H1>\n";
    print "<P><I>This page shows the differences between two files.</I></P>\n";
    &diff2($file1, $rev1, $file2, $rev2);
}
    

################################
#
# None of the above.
#
################################

else {
    &bail( "Invalid invocation @ARGV" );
}

# Trailer

@HTMLFOOTER = (
    "<hr><p align=center>",
    &url ("", "Top"), " | ",
    &url ("\@clients", "Clients"), " | ",
    &url ("\@users", "Users"), " | ",
    &url ("\@branches", "Branches"), " | ",
    &url ("\@labels", "Labels"), " | ",
    &url ("\@jobs", "Jobs"),
    @OTHER_FOOTERS,
    "</p></body>\n");

print   @HTMLFOOTER;

##################################################################
##################################################################
#
# Subroutines.
#
##################################################################
##################################################################
sub printfile {
    print 
    @HTMLHEADER,
    "<title>File $name</title>\n",
    "<center><h1>File $name#$rev</h1></center>\n",
    "<i>This form shows you the raw contents of a file, as long as \n",
    "it isn't binary.</i>",
    "<hr>\n";
    printfile_inner (@_);
}

sub printfile_inner {
  local ($name, $rev, $targetLine) = @_;
   &p4open( 'P4', "print \"$name#$rev\"|" );

    # Get header line
    # //depot/main/jam/Jamfile#39 - edit change 1749 (text)

    $_ = <P4>;
    local( $name, $rev, $type ) = m!^(\S+)\#(\d+) - \S+ \S+ \S+ \((\w+)\)!;

    if( $type eq "binary" || $type eq "xbinary" )
    {
    print "<h2>$type</h2>\n";
    }
    else
    {
    print "<pre>\n";

    $line = 0;
    while( <P4> ) {
        $line++;
        
        if($ARGV[0] eq "\@list") {
            printf '%5d  ', $line;
        }
        print "<B>" if(defined($targetLine) && $line == $targetLine);
        s/&/&amp;/g;
        s/\"/&quot;/g;
        s/</&lt;/g;
        s/>/&gt;/g;
        printf '<A NAME="%d">%s</A>', $line, $_;
        print "</B>" if(defined($targetLine) && $line == $targetLine);
    }

    print "</pre>\n";
    }

    close P4;
}

sub url {
    local( $url, $name ) = @_;
    $url =~ s/ /%20/g;
    return qq(<a HREF="$localname?$url">$name</a>) ;
}

sub mailto {
#   local( $uname ) = @_;
    return qq(<a HREF="mailto:@_">@_</a>) ;
}

sub bail {
    print @HTMLERROR, @_, "\n";
    die @_;
}

sub p4open {
    local( $handle, @command ) = @_;
    open( $handle, "D:/Program\\ Files/Perforce/p4.exe @command" ) || &bail( "p4 @command failed" );
}

# Support for processing diff chunks.
#
# skip: skip lines in source file
# display: display lines in source file, handling funny chars 
# catchup: display & skip as necessary
#

sub skip {
    local( $handle, $to ) = @_;

    while( $to > 0 && ( $_ = <$handle> ) ) {
        $to--;
    }

    return $to;
}

sub display {
    local( $handle, $to ) = @_;
    local $res = "";

    while( $to-- > 0 && ( $_ = <$handle> ) ) {

        if (/[&<>]/) 
        {
        s/&/\&amp;/g;
        s/</\&lt;/g;
        s/>/\&gt;/g;
        }
        $res .= $_;
    }
    return $res;
}

sub catchup {

    local( $handle, $to ) = @_;

    if( $to > $MAXCONTEXT )
    {
        local( $skipped ) = $to - $NCONTEXT * 2;

        print &display( $handle, $NCONTEXT );
        
        $skipped -= &skip( $handle, $skipped );

        print 
        "<hr><center><strong>",
        "$skipped lines skipped",
        "</strong></center><hr>\n" if( $skipped );

        print &display( $handle, $NCONTEXT );
    }
    else
    {
        print &display;
    }
}



sub diff2 {
    print @DIFFHEADER, "<hr>";
        
    &diff2_inner (@_);
}

sub diff2_inner {
    local($name1, $rev1, $name2, $rev2) = @_;
    local @lines;
    local $nchunk = 0;
    local( @dels, @adds);

    &p4open('P4', "diff2 \"$name1#$rev1\" \"$name2#$rev2\" |");
    $_ = <P4>; 

    while (<P4>) {
        local( $dels, $adds );
        local( $la, $lb, $op, $ra, $rb ) = 
            /(\d+),?(\d*)([acd])(\d+),?(\d*)/;

        next unless $ra;

        if( !$lb ) { $lb = $op ne 'a' ? $la : $la - 1; }
        if( !$rb ) { $rb = $op ne 'd' ? $ra : $ra - 1; }

        $start[ $nchunk ] = $op ne 'd' ? $ra : $ra + 1;
        $dels[ $nchunk ] = $dels = $lb - $la + 1;
        $adds[ $nchunk ] = $adds = $rb - $ra + 1;
        $lines[ $nchunk ] = ();

        # deletes

        while( $dels-- ) {
            $_ = <P4>;      
            s/^. //;
            if (/[&<>]/) {
                s/&/\&amp;/g;
                s/</\&lt;/g;
                s/>/\&gt;/g;
            }
            $lines[ $nchunk ] .= $_;
        }
        
        # separator

        if ($op eq 'c') {   
            $_ = <P4>; 
        }

        # adds

        while( $adds-- ) {
            $_ = <P4>;
        }

        $nchunk++;
    }

    close P4;

    # Now walk through the diff chunks, reading the current rev and
    # displaying it as necessary.

    local( $curlin ) = 1;
    print "<pre>\n";
    &p4open('P4', "print -q \"$name2#$rev2\"|");

    for( $n = 0; $n < $nchunk; $n++ )
    {
        # print up to this chunk.

        &catchup( 'P4', $start[ $n ] - $curlin );

        if(!$adds[$n] && $dels[$n]) {
            print "${DEL}$lines[$n]$DELEND";
        } elsif($adds[$n] && !$dels[$n]) {
            print "$ADD", &display( 'P4', $adds[ $n ] ), "$ADDEND";
        } elsif($adds[$n] && $dels[$n]) {
      $addLines = &display( 'P4', $adds[ $n ] ) if($adds[$n]);
      $delLines = $lines[$n] if($dels[$n]);

      # Unfortunately we now have to duplicate diff's work
      # to find the whitespace-only portions of this change.
      # We do this using the normal Longest Common Subsequence
      # algorithm.

      local(%L, $i, $j);
      local @A = split(/\n/, $addLines);
      local @A2 = @A;
            map s/\s+/ /g, @A2;
            map s/\s+$//g, @A2;
      local @B = split(/\n/, $delLines);
      local @B2 = @B;
            map s/\s+/ /g, @B2;
            map s/\s+$//g, @B2;

      for($i = $#A + 1; $i >= 0; $i--) {
    for($j = $#B + 1; $j >= 0; $j--) {
        if($i > $#A || $j > $#B) {
      $L{$i,$j} = 0;
        } elsif($A2[$i] eq $B2[$j]) {
      $L{$i,$j} = $L{$i + 1, $j + 1} + 1;
        } else {
      $L{$i,$j} = $L{$i + 1, $j} > $L{$i, $j + 1} ?
          $L{$i + 1, $j} : $L{$i, $j + 1};
        }
    }
      }

            # Print out L as table.
            if($Debug) {
                print STDERR "     ";
                for($j = 0; $j <= $#B+1; $j++) {
                    printf STDERR "%4d", $j;
                }
                print STDERR "\n";
                for($i = 0; $i <= $#A+1; $i++) {
                    printf STDERR "%4d:", $i;
                    for($j = 0; $j <= $#B+1; $j++) {
                        printf STDERR "%4d", $L{$i,$j};
                    }
                    print STDERR "\n";
                } 
                print STDERR "\n";
            }

      $i = $j = 0; 
      while($i <= $#A || $j <= $#B) {
    if($j <= $#B && $L{$i, $j} == $L{$i, $j + 1}) {
        print "${DEL}$B[$j]$DELEND\n";
        ++$j;
    } elsif($i <= $#A && $L{$i, $j} == $L{$i + 1, $j}) {
        print "${ADD}$A[$i]$ADDEND\n";
        ++$i;
    } elsif($i <= $#A && $j <= $#B && 
                    $L{$i, $j} > $L{$i + 1, $j + 1}) {
        print "${WS}$A[$i]$WSEND\n";
        ++$i;
        ++$j;
    }
      }
        } 
        $curlin = $start[ $n ] + $adds[ $n ];
    }

    &catchup( 'P4', 999999999 );

    close P4;
    print "</PRE>\n";
}

sub lead2 {
    local($a, $b) = @_;
    local $i;

    for($i = 0; 
        $i < length($a) && substr($a, $i, 1) eq substr($b, $i, 1); 
        $i++) { }

    return $i;
}
            
# This method returns the string that is a common prefix to all arguments.
# Unhelpful implementation.
sub lead {
    local $res;

    local($arg, $len);
    foreach $arg (@_) {
        print STDERR ">$res< >$arg<\n";
        if(defined($arg)) {
            if(defined($res)) {
                $len = &lead2($res, $arg);
                if($len < length($res)) {
                    $res = substr($res, 0, $len);
                }
            } else {
                $res = $arg;
            }
        }
    }
    return $res;
}
