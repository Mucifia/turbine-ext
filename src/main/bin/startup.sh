#!/usr/bin/env bash
## resolve links
PRG="$0"

# need this for relative symlinks
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="`dirname "$PRG"`/$link"
  fi
done

filelocation=`pwd`

AW_COMPONENT_HOME=`dirname "$PRG"`/..

# make it fully qualified
AW_COMPONENT_HOME=`cd "$AW_COMPONENT_HOME" && pwd`

EOS_COMPONENT_HOME=`cd "$AW_COMPONENT_HOME" && cd ".." && pwd`

cd "$filelocation"

# =============== Please do not modify the following content =============== #

if [ "$(uname)" == "Darwin" ]; then
    windows="0"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    windows="0"
elif [ "$(expr substr $(uname -s) 1 5)" == "MINGW" ]; then
    windows="1"
else
    windows="0"
fi

EXEC_JAR_NAME="Turbine_Server.jar"

DIR_BIN=${AW_COMPONENT_HOME}/bin
DIR_LOG=${AW_COMPONENT_HOME}/log
DIR_LIB=${AW_COMPONENT_HOME}/libs
DIR_CONF=${AW_COMPONENT_HOME}/config

FILE_LOG=${DIR_LOG}/turbine.log
FILE_LIB=${DIR_LIB}/${EXEC_JAR_NAME}
FILE_PID=${DIR_LIB}/turbine.pid


JRE_LOCATION=${EOS_COMPONENT_HOME}/jre

function checkJavaVersion {
  if [[ -n "$JRE_LOCATION" ]] && [[ -x "$JRE_LOCATION/bin/java" ]];  then
      if [ "$windows" == "1" ]; then
        tmp_java_home=`cygpath -sw "$JRE_LOCATION"`
        export JAVA_HOME=`cygpath -u $tmp_java_home`
        echo "Windows new JRE_LOCATION is: $JRE_LOCATION"
      fi
      _java="$JRE_LOCATION/bin/java"
  elif type -p java > /dev/null; then
    _java=java
  else
      echo "Could not find java executable, please check your java environment"
    exit 1
  fi
  if [[ "$_java" ]]; then
      version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
      if [[ "$version" < "1.8" ]]; then
          echo "Java version is $version, please make sure java 1.8+ is in the path"
         exit 1
      fi
  fi
}

checkJavaVersion


if [[ -f "$FILE_LOG" ]]; then
rm -rf "$FILE_LOG"
fi

if [[ -f "$FILE_PID" ]]; then
rm -rf "$FILE_PID"
fi

export JAVA_OPTS="-Dlogging.file=${FILE_LOG} -Dspring.pid.file=${FILE_PID} -Dspring.config.location=$DIR_CONF/"

cd ${DIR_LIB}

exec "$_java" $JAVA_OPTS -jar "$FILE_LIB"


