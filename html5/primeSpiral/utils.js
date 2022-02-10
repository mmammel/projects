/*
  Utility for reading "query string parameters" from the URL, i.e. :
    http://www.mjmtools.com/bridges/bridges.html?debug=true

  getUrlParameter( "debug" ) -> "true"
  getUrlParameter( "foobar" ) -> null
  */
  function getURLParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? null : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };
