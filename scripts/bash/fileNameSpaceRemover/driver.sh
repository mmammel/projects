#!/bin/bash

  # Remove spaces from the names
  find ${INBOX_DIR} -name "*.dat" -print | xargs -Ifoo ./removeSpaces.sh foo

