#!/bin/bash

counter=0;
  while [ $counter -lt 100 ]; do
    # Call the API and receive the eticket ID
    ETICKETID=`wget -q -O - --no-check-certificate "https://webtest.skillcheck.com/onlinetesting/servlet/com.skillcheck.session_management.SK_Servlet?ID=MARC3C&MODE=auto_eticket,1,MTS_0713B,SHOW_START,NO_APPINFO,CLIENT_TRACKING_ID,${counter}"`
  let counter=counter+1;
  echo $ETICKETID;
done
