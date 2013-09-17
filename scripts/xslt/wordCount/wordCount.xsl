<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
  <xsl:variable name="description"><xsl:value-of select="myfile/description"/></xsl:variable>
  <xsl:variable name="description2"><xsl:value-of select="myfile/description2"/></xsl:variable>
  <html>
  <body>
    <h2><xsl:value-of select="$description"/></h2>
    <h2><xsl:value-of select="string-length(normalize-space($description)) - string-length(translate(normalize-space($description),' ','')) + 1"/></h2>
    <h2>
      <xsl:call-template name="wordTrim">
        <xsl:with-param name="wcount" select="45"/>
        <xsl:with-param name="words" select="normalize-space($description)"/>
      </xsl:call-template>
    </h2>
    <h2>
      <xsl:call-template name="wordTrim">
        <xsl:with-param name="wcount" select="45"/>
        <xsl:with-param name="words" select="normalize-space($description2)"/>
      </xsl:call-template>
    </h2>
  </body>
  </html>
</xsl:template>

  <xsl:template name="wordTrim">
    <xsl:param name="wcount"/>
    <xsl:param name="words"/>
    <xsl:if test="$wcount &gt; 0">
      <xsl:choose>
        <xsl:when test="contains($words,' ')">
          <xsl:value-of select="substring-before($words,' ')"/>
          <xsl:text> </xsl:text>
          <xsl:call-template name="wordTrim">
           <xsl:with-param name="wcount" select="$wcount - 1"/>
           <xsl:with-param name="words" select="substring-after($words,' ')"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$words"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
