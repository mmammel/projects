# ~/.bash_profile: executed by bash for login shells.

if [ -e /etc/bash.bashrc ] ; then
  source /etc/bash.bashrc
fi

if [ -e ~/.bashrc ] ; then
  source ~/.bashrc
fi

# Set PATH so it includes user's private bin if it exists
# if [ -d ~/bin ] ; then
#   PATH="~/bin:${PATH}"
# fi

# Set MANPATH so it includes users' private man if it exists
# if [ -d ~/man ]; then
#   MANPATH="~/man:${MANPATH}"
# fi

# Set INFOPATH so it includes users' private info if it exists
# if [ -d ~/info ]; then
#   INFOPATH="~/info:${INFOPATH}"
# fi

#PS1="\u@\h:\w\n$ "
#PS1="\u@\h:\w\$ "
# setup bash color for hostname
#PS1="\u@\[\033[0;0m\]\h\[\033[0m\]: \w\$ "

alias a='man -k'
alias logout='dir_persist ; logout'
alias be='$EDITOR ~/.bashrc ; source ~/.bashrc'
alias f=finger
alias h=history
alias j=jobs
alias ls='ls -F'
alias ll='ls -l'
alias la='ls -al'
alias cp='cp -i'
alias m='less -a -i -P='
alias which='type -all'
alias xman='xman -bothshown -notopbox &'
alias mon='monitor -top'
alias du='du -k'
alias llm='ls -l | more'
alias emacs='emacs -bg White'
alias grep='grep -n'
alias tail='tail -f'

#MY_IP=`ipconfig | command grep "IP Address" | cut -d":" -f2 | cut -d" " -f2`

export TMP=/tmp

export ANT_OPTS='-Xmx1024m -Xms1024m'

# I set these through windows so they would be seen by eclipse too.
export P4USER=mmammel
export P4CLIENT=p4work
export P4PORT=butkus.homelinux.com:1666
export P4EDITOR=vi

#export CVSROOT=:pserver:mmammel@storm.eng.veritas.com:/space/cvsroot
JAVAS=(`find /d/opt/Java -maxdepth 1 -mindepth 1 -type d | cut -d'/' -f5 | sort`);
javahome=/d/opt/Java/jdk1.6.0_24

let index=1;
for javaname in ${JAVAS[@]}; do
  echo "$index) $javaname"
  let index=index+1;
done
echo -n "Choose Sun jdk [enter chooses 1]:"
read sunjdk;
if [ "x$sunjdk" = "x" ]; then sunjdk=1; fi
let sunjdk=sunjdk-1;
if [ $sunjdk -ge ${#JAVAS[@]} ] || [ $sunjdk -lt 0 ]; then
 sunjdk=1
fi 
javahome="/d/opt/Java/${JAVAS[$sunjdk]}";
grailshome=/d/opt/grails-1.2.1
echo "1) Grails 1.2.1"
echo "2) Grails 1.1.1"
echo "3) Grails 1.0.4"
echo -n "Choose Grails SDK [enter chooses 1]:"
read grailssdk
case $grailssdk in
        "")  ;;
        "1") grailshome=/d/opt/grails-1.2.1
             ;;
        "2") grailshome=/d/opt/grails-1.1.1
             ;;
        "3") grailshome=/d/opt/grails-1.0.4
             ;;
        "*") echo Unrecognized option $grailssdk
             ;;
esac

groovyhome=/d/opt/groovy-1.6.3
export JAVA_HOME=`cygpath -p -w $javahome`
export GROOVY_HOME=`cygpath -p -w $groovyhome`
export GRAILS_HOME=`cygpath -p -w $grailshome`
TEXTPAD='/c/Program Files/TextPad 5'
export PATH="$TEXTPAD":$javahome/bin:/d/opt/google/depot_tools:$grailshome/bin:$groovyhome/bin:/d/opt/apache-ant-1.7.0/bin:$PATH
unset CLASSPATH
unset QTJAVA

export PATH=$PATH:$HOME/bin

function grecfind {
    if [ $# -eq 1 ]; then
        findStr=${1};
        fileSpec=*;
    else
        findStr=${2};
        fileSpec=${1};
    fi

    find . -name "${fileSpec}" -print | xargs grep -n ${findStr};
} 2>/dev/null

function findClass {
  echo "Looking in jars..."
  for jarfile in `find . -name "*.jar" -print`; do
    FOUND=(`jar tf $jarfile | grep "$1"`);
    if [ ! -z ${FOUND[0]} ]; then
      echo "Found in $jarfile:";
      for found in ${FOUND[@]}; do
        echo "$found";
      done
    fi
  done
  echo "...done.  Looking on filesystem..."
  find . -name "*$1*.class" -print
  echo "...done."
} 2>/dev/null

function findPackage {
  PACKAGE=`echo "$1" | tr '.' '/'`
  echo "Looking in jars..."
  for jarfile in `find . -name "*.jar" -print`; do
    FOUND=(`jar tf $jarfile | grep "$PACKAGE"`);
    if [ ! -z ${FOUND[0]} ]; then
      echo "Found in $jarfile:";
      for found in ${FOUND[@]}; do
        echo "$found";
      done
    fi
  done
  echo "...done.  Looking on filesystem...";
  find . -name "*.class" -print | grep "$PACKAGE"
  echo "...done."
} 2>/dev/null

function mfind {
  currentDir=`pwd`;
  if [ $# -eq 1 ]; then
    find ${currentDir} -name "${1}" -print;
  else
    echo "Error: Usage: mfind <searchPattern>"
  fi

} 2>/dev/null

function recfind {
    find . -name "*.[ch]*" -print | xargs grep -n "$1";
} 2>/dev/null

function jrecfind {
    find . -name "*.java" -print | xargs egrep -n "$1";
} 2>/dev/null

function xrecfind {
    find . -name "*.xml" -print | xargs egrep -n "$1";
} 2>/dev/null

function findgrep {
    if [ -d $1 ] ; then
        dir=$1
        shift
    else
        dir=.
    fi
    if [ -z $1 ] ; then
        echo >&2 "Usage: findgrep [dir] [grep-flags] pattern extension"
    else
      if [ $# -eq 3 ] ; then
        grep -n $1 $2 $(find $dir -name $3) | cut -c1-78
      else
        grep -n $1 $(find $dir -name $2) | cut -c1-78
      fi
    fi
} 2>/dev/null

CD_USAGE="cd: Usage: cd [ -s [ 1-5 | [1-5]=<directory> ] | -p | <directory name> | [1-5] ]";

function dir_persist {
     DIRSTRINGS="ONE_DC TWO_DC THREE_DC FOUR_DC FIVE_DC";
    _DIRSTRINGS=($DIRSTRINGS);

    if [ $# -eq 0 ]; then
      echo -n "Enter a name to persist dirs under, or <enter> to skip: "
      read session_tag
    else
      session_tag=${1}
    fi

    if [ "${session_tag}" = "" ]; then
        CDFILE_NAME=".cdfile_${HOSTNAME}_default"
    else
        CDFILE_NAME=".cdfile_${HOSTNAME}_${session_tag}" 
    fi

    let count=0;
    while [ $count -lt 5 ] ; do
      outp=`env | grep "${_DIRSTRINGS[$count]}"`;
      if [ "" != "$outp" ] ; then
        dir_to_print=`echo $outp | cut -d"=" -f2`;
        let num=count+1;
        if [ $count -eq 0 ] ; then
            echo "$num=$dir_to_print" > ~/${CDFILE_NAME};
        else
            echo "$num=$dir_to_print" >> ~/${CDFILE_NAME};
        fi
      fi
      let count=count+1;
    done
}

function dirset {
     DIRSTRINGS="ONE_DC TWO_DC THREE_DC FOUR_DC FIVE_DC";
    _DIRSTRINGS=($DIRSTRINGS);
 let err=0;

 if [ "$#" -ge 2 ] || [ "$#" -eq 0 ] ; then
   let err=1;
 fi
 if [ -z `echo "$1" | grep "="` ] ; then
    let reg=$1;
    let pwd_flag=1;
 else
    let reg=`echo "$1" | cut -d"=" -f1`;
    dir=`echo "$1" | cut -d"=" -f2 | tr '~' ' '`;
    let pwd_flag=0;
 fi
   
 if [ $reg -gt 5 ] || [ $reg -eq 0 ] ; then
   echo >&2 "cd: Invalid directory choice";
   let err=1;
 fi
 if [ $err -eq 0 ] ; then
    let reg=reg-1;
    if [ $pwd_flag -eq 1 ] ; then
        littlePWD="${PWD}"
        export "${_DIRSTRINGS[$reg]}=${littlePWD}";
    else
        littleDir="${dir}"
        export "${_DIRSTRINGS[$reg]}=${littleDir}";
    fi
 fi
}

function cd {
  DIRSTRINGS="ONE_DC TWO_DC THREE_DC FOUR_DC FIVE_DC"
  _DIRSTRINGS=($DIRSTRINGS)
  let err=0

case $# in
0)
    command cd
     return
;;
1)
    if [ "$1" = "-p" ] ; then
      let count=0
       while [ "$count" -lt 5 ] ; do
         outp=`env | grep "${_DIRSTRINGS[$count]}"`
         if [ "" != "$outp" ] ; then
           dir_to_print=`echo $outp | cut -d"=" -f2`
           let num=count+1
           echo "$num) $dir_to_print"
         fi
         let count=count+1
       done
     elif [ "$1" = 1 -o "$1" = 2 -o "$1" = 3 -o "$1" = 4 -o "$1" = 5 ] ; then
      let choice=$1
      let choice=choice-1
      _CD_STRING="\$${_DIRSTRINGS[$choice]}"
      CD_STRING=`eval echo "${_CD_STRING}"`
      command cd "${CD_STRING}"
     else
      command cd "${1}"
      return
     fi
;;
2)
    teststr=`echo $1 | grep "\-s"`
   if [ -z "$teststr" ] ; then
      echo >&2 "$CD_USAGE"
      let err=1
      return
   fi
      dirset "$2"
;;
*)
     echo >&2 "$CD_USAGE"
     let err=1
     return
;;
esac

}


function dirmanager {

  showList="true";
  while [ 1 ]; do

    cdFileNames=(`find ~ -maxdepth 1 -name ".cdfile*" -printf "%f\n"`)
    if [ "${showList}" = "true" ]; then
      let loopCount=0
      for fileName in ${cdFileNames[@]}; do
        let loopCount=${loopCount}+1
        trimmedName=`echo ${fileName} | cut -d'_' -f2-`
        echo "(${loopCount}) ${trimmedName}"
      done
    fi
    echo ""
    echo "Please enter a command [ \"quit\" to exit ]: "
    echo -n "> "

    read input
  
    if [ "$input" = "q" -o "$input" = "quit" ]; then
      return;
    fi

    inputArray=(${input})
    cmdStrCount=${#inputArray[@]}

    case ${cmdStrCount} in

    0)
      showList="true"
      ;;

    1)
      commandStr=${inputArray[0]};
      if [ "${commandStr}" = "h" ]; then
        printManagerHelp
        showList="false";
      elif [ "${commandStr}" = "ls" ]; then
        showList="true"
      fi 
      ;;

    2)
      commandStr=${inputArray[0]};
      targetStr=${inputArray[1]};

      if [ ${targetStr} -gt ${loopCount} ]; then
        echo "invalid second argument"
        printManagerHelp
        showList="false";
      else
        let targetStr=${targetStr}-1;
        targetFileName=${cdFileNames[${targetStr}]}

        if [ "${commandStr}" = "r" ]; then
          rm ${targetFileName}
          showList="true"
        elif [ "${commandStr}" = "s" ]; then
          dirArgs=(`cat ~/${targetFileName} | tr " " '~'`)
          for argSet in ${dirArgs[@]}; do
            dirset "${argSet}"
            echo -n "."
          done
          echo "Done"
          showList="false"
        fi
      fi
      ;;

    *)
      showList="true"
      ;;
    esac

  done

}

printManagerHelp() {

  echo "  Directory Manager v1.0"
  echo "   commands:"
  echo "       s N   - sets the current directory set to that corresponding to N in the menu"
  echo "       r N   - removes directory set N from the menu permanently"
  echo "       h     - displays this message"

}

#Persist directories
#echo "Choose a tag to persist, or <enter> to skip:"
cdFileNames=(`find ~ -maxdepth 1 -name ".cdfile_*" -printf "%f\n"`)
let loopCount=0
for fileName in ${cdFileNames[@]}; do
    let loopCount=${loopCount}+1
    trimmedName=`echo ${fileName} | cut -d'_' -f2-`
    echo "(${loopCount}) ${trimmedName}"
done
echo ""
echo -n "Please enter which dir set to use [<enter> to exit]: "
read setSelection 
if [ "$setSelection" = "" ]; then
  touch .cdfile_empty
  CDFILE_NAME=".cdfile_empty"
else
  if [ "${setSelection}" -gt "${loopCount}" ]; then
    echo "Invalid selection"
    touch .cdfile_empty
    CDFILE_NAME=".cdfile_empty"
  else
    let setSelection=${setSelection}-1;
    CDFILE_NAME=${cdFileNames[${setSelection}]}
    echo ""
    echo -n "Persisting Directories"
  fi
fi 

dirArgs=(`cat ~/${CDFILE_NAME} | command grep = | tr " " '~'`)
for dirarg in "${dirArgs[@]}"; do
  dirset "${dirarg}";
  echo -n "."
done
echo "Done"
echo ""
echo ""

function editvi {
    p4 edit $*
    vi $*
}


function editemacs {
    p4 edit $*
    emacs $* &
}


function crec {
  CREC_SOURCES=".crec_sources"
  CREC_TARGETS=".crec_targets"

  touch ~/${CREC_TARGETS}
  touch ~/${CREC_SOURCES}

  if [ "$1" = "reset" ]; then
    echo "resetting crec file...all recordings will be lost"
    rm -f ~/${CREC_SOURCES};
    rm -f ~/${CREC_TARGETS};
  elif [ "$1" = "replay" ]; then
    echo "replaying crec copies..."
    SOURCES=(`cat ~/${CREC_SOURCES}`);
    TARGETS=(`cat ~/${CREC_TARGETS}`);
    let linecount=0;
    for _source in ${SOURCES[@]}; do
      target=`echo ${TARGETS[${linecount}]} | tr '%' ' '`;
      source=`echo ${SOURCES[${linecount}]} | tr '%' ' '`;
      cp "${source}" "${target}";
      let linecount=${linecount}+1;
    done
    echo "...done";
  else
    echo "${PWD}/${1}";
    echo "${PWD}/${1}" | tr ' ' '%' >> ~/${CREC_SOURCES};
    echo "${PWD}/${2}" | tr ' ' '%' >> ~/${CREC_TARGETS};
    cp "${1}" "${2}"
  fi
}
    

# setup xterm title bar

TITLE_BAR_LABEL=`echo ${CDFILE_NAME} | cut -d'_' -f3-`

case $TERM in
    xterm*)
        echo -ne "\033]0;${LOGNAME}@${HOSTNAME}:${TITLE_BAR_LABEL}\007"
        ;;
    *)
        ;;
esac

export PATH



