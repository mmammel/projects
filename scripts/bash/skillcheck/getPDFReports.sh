#!/bin/bash

#for eticket in `cat etickets.dat`; do
#  wget -q --no-check-certificate -O report_${eticket}.pdf "https://webtest.skillcheck.com/onlinetesting/servlet/com.skillcheck.session_management.SK_Servlet?ID=MARC3C&MODE=REPORTRETRIEVAL,$eticket,pdf"
#done

#for testAndID in `cat testAndIDs.dat`; do
#  wget -q --no-check-certificate -O report_${testAndID}.pdf "https://gamma.skillcheck.com/onlinetesting/servlet/com.skillcheck.session_management.SK_Servlet?ID=MARC3C&MODE=REPORTRETRIEVAL,testAndID.${testAndID},pdf"
#done

for scoreKey in `cat scoreKeys.dat`; do
  wget -q --no-check-certificate -O report_${scoreKey}.pdf "https://gamma.skillcheck.com/onlinetesting/servlet/com.skillcheck.session_management.SK_Servlet?ID=MARC3C&MODE=REPORTRETRIEVAL,scoreKey.${scoreKey},pdf"
done
