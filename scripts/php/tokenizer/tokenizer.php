<?php
  function startsWith( $s, $prefix ) {
	  preg_match('/^'.$prefix.'/', $s, $matches );
	  return count($matches) > 0;
  }

  function endsWith( $s, $suffix ) {
	  preg_match('/'.$suffix.'$/', $s, $matches );
	  return count($matches) > 0;
  }

  function isWrappedInQuotes($s) {
	  preg_match( '/^".*"$/', $s, $matches );
	  return count($matches) > 0;
  }

  function tokenize( $keywords ) {
    $retVal = array();
    $inQuote = false;
    $currToken = "";
    // normalize the whitespace around any "or"s
    //$keywords = preg_replace("[ ]+(?:or|OR)[ ]+", " or ", $keywords );
    $keywords = preg_replace("/[ ]+/", " ", $keywords );
    // first just split, then we cycle through them and glue together a token if need be.
    $rawTokens = explode(" ",$keywords);
    foreach( $rawTokens as $t ) {
      if( startsWith($t,'"') && endsWith($t,'"') ) {
        if( $inQuote ) {
          // weird nested quote... just flush and carry on.
          if( strlen($currToken) > 0 ) {
            $retVal[] = $currToken;
          }
          $currToken = "";
          $inQuote = false;
        }
        // it's an "atomic" quoted string, just add it, we are NOT in a quote.
        $retVal[] = $t;
        $currToken = "";
      } else if( startsWith($t, '"') || startsWith($t, '-"') ) {
        if( $inQuote ) {
          // someone is nesting quotes on us...
          // just flush the currtoken and start fresh.
          $retVal[] = $currToken;
          $currToken = "";
        }

        $inQuote = true;
        $currToken = $t;
      } else if( endsWith($t,'"') ) {
        if( $inQuote ) {
          $currToken = $currToken." ".$t;
          $inQuote = false;
          $retVal[] = $currToken;
          $currToken = "";
        } else {
          // someone screwed the pooch, just add this as a token, it will get literally searched
          $retVal[] = $t;
          $currToken = "";
        }
      } else {
        if( $inQuote ) {
          $currToken = $currToken." ".$t;
        } else {
          $retVal[] = $t;
          $currToken = "";
        }
      }
    }

    if( strlen($currToken) > 0 ) {
	    $retVal[] = $currToken;
    }

    return $retVal;
  }

  function escapeElasticReservedChars($string,$includeQuotes) {
    if( $includeQuotes ) {
      $regex = "/[\\+\\-\\=\\&\\|\\!\\(\\)\\{\\}\\[\\]\\\"\\^\\~\\*\\<\\>\\?\\:\\\\\\/]/";
    } else {
      $regex = "/[\\+\\-\\=\\&\\|\\!\\(\\)\\{\\}\\[\\]\\^\\~\\*\\<\\>\\?\\:\\\\\\/]/";
    }
    return preg_replace($regex, addslashes('\\$0'), $string);
  }

  function buildKeywordQueryString( $keywords ) {
	$keywords = trim($keywords);
	$retVal = "(";
	$first = true;
  	$keywords = preg_replace("/[ ]+(?:or|OR)[ ]+/", " or ", $keywords );
	$queries = explode( " or ", $keywords );
	foreach( $queries as $q ) {
		if( $first ) {
			$first = false;
		} else {
			$retVal = $retVal."OR";
		}
		$retVal = $retVal."(";

		$tokens = tokenize($q);
		$firstToken = true;
		$negative = false;
		foreach( $tokens as $t ) {
			if( startsWith($t,"-") ) {
				$negative = true;
				$t = substr($t,1);
			} else {
				$negative = false;
			}


			if( $firstToken ) {
				// we are first - no AND required, but check if we are negative.
				if( $negative ) {
					$retVal = $retVal . "NOT(";
				} else {
					$retVal = $retVal . "(";
				}
				$firstToken = false;
			} else {
				if( $negative ) {
					$retVal = $retVal . "AND NOT(";
				} else {
					$retVal = $retVal . "AND(";
				}
			}

			$retVal = $retVal . escapeElasticReservedChars($t, !isWrappedInQuotes($t) );

			$retVal = $retVal . ")";

		}

		$retVal = $retVal . ")";
	}
	$retVal = $retVal . ")";

	return $retVal;
  }

  //var_dump( tokenize( '"foobar foo" max mammel -jomama' ) );
  //var_dump( tokenize( 'foobar foo" max mammel -jomama' ) );
  //var_dump( tokenize( '"foobar foo max mammel -jomama' ) );
  //var_dump( tokenize( '"foobar foo" max mammel -jomama' ) );
  //var_dump( tokenize( '"foobar foo" "max mammel" -jomama' ) );
  //var_dump( tokenize( '"foobar foo" max "mammel" -jomama' ) );
  //var_dump( tokenize( '"foobar "foo"" max mammel -jomama' ) );
  //var_dump( tokenize( '"foobar foo" max"" mammel -jomama' ) );

  //var_dump( isWrappedInQuotes('"jomama') );
  //var_dump( isWrappedInQuotes('"jomama"') );
  //var_dump( isWrappedInQuotes('"jomama is yomama"') );
  //var_dump( isWrappedInQuotes('jomama"') );

  var_dump( buildKeywordQueryString('"windows 2000" -FILES OR windows OR jomama -foobar yomama') );
  var_dump( buildKeywordQueryString('"windows 2000" -FILES OR "windows OR" jomama -foobar yomama') );
  var_dump( buildKeywordQueryString('"windows 2000" -FILES or windows OR jomama -foobar yomama') );
  var_dump( buildKeywordQueryString('"windows 2000" -FILES OR "Windows XP" OR -Food') );
  var_dump( buildKeywordQueryString('2010 -"file Management"') );


?>
