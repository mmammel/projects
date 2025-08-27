<?php
function transformXMLWithXSLT($xmlString, $xsltString) {
    // Load XML string into a DOMDocument
    $xml = new DOMDocument();
    if (!$xml->loadXML($xmlString)) {
        return "Error: Failed to load XML string.";
    }

    // Load XSLT string into a DOMDocument
    $xslt = new DOMDocument();
    if (!$xslt->loadXML($xsltString)) {
        return "Error: Failed to load XSLT string.";
    }

    // Create an XSLT processor
    $proc = new XSLTProcessor();
    $proc->importStylesheet($xslt); // Load the XSL stylesheet

    // Apply the transformation
    $result = $proc->transformToXML($xml);

    return $result !== false ? $result : "Error: Transformation failed.";
}

// Example XML string
$xmlString = <<<XML
<books>
    <book>
        <title>Harry Potter</title>
        <author>J.K. Rowling</author>
    </book>
    <book>
        <title>The Hobbit</title>
        <author>J.R.R. Tolkien</author>
    </book>
</books>
XML;

// Example XSLT string
$xsltString = <<<XSL
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <body>
                <h2>Book List</h2>
                <ul>
                    <xsl:for-each select="books/book">
                        <li>
                            <b><xsl:value-of select="title"/></b> - <xsl:value-of select="author"/>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
XSL;

// Run transformation
$result = transformXMLWithXSLT($xmlString, $xsltString);
echo $result;
?>
