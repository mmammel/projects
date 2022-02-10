#!/bin/bash

cat scrabble_words.txt | egrep "^[hmscpboidtnle]{6,}$" | egrep "ss|pp|oo|tt|bb|dd|nn|ll|ee|mm" | egrep -v "h[dpntile]|m[ctbdnel]|i[snhdclb]|e[dpbcsmh]|l[cthspim]|b[snmptei]|s[bidtnle]|p[hcnebdl]|t[ldsmbch]|n[bcpihsm]|d[hstempi]|c[lnpmtie]"
