<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" omit-xml-declaration="no" indent="yes"/>
	<xsl:include href="http://xml.postmasterlx.com/common/xsl/StandardBatchTemplates.xsl"/>
	<!-- Message Field Paramarters -->
	<!--  Standard Mappings  -->

	<!-- Board Mappings -->
	<!-- Templates -->
	<xsl:template match="/BatchFeed">
		<source>
			<publisher></publisher>
			<publisherurl></publisherurl>
			<lastBuildDate></lastBuildDate>
			<xsl:apply-templates select="Postings/Posting"/>
		</source>
	</xsl:template>
	<xsl:template match="Posting">
		<job>
			<source>
				<xsl:value-of select="Client/Name"/>
			</source>
			<title>
				<xsl:value-of select="Title"/>
			</title>
			<date>
				<xsl:value-of select="StartDate"/>
			</date>
			<referencenumber>
				<xsl:value-of select="PublicId"/>
			</referencenumber>
			<url>http://appclix.postmasterlx.com/index.html?pid=<xsl:value-of select="@id"/>&amp;source=indeed</url>
			<company>
				<xsl:value-of select="Client/Name"/>
			</company>
			<city>
				<xsl:value-of select="Locations/Location/City"/>
			</city>
			<state>
				<xsl:value-of select="Locations/Location/Region"/>
			</state>
			<country>
				<xsl:value-of select="Locations/Location/Country"/>
			</country>
			<postalcode>
				<xsl:value-of select="Locations/Location/PostalCode"/>
			</postalcode>
			<description>
				<xsl:value-of select="Unit/Header"/>
				<xsl:value-of select="Description"/>
				<xsl:if test="Qualifications/text()">&lt;BR/&gt;&lt;BR/&gt;Qualifications:
					<xsl:value-of select="Qualifications"/>
				</xsl:if>
				<xsl:if test="Benefits/text()">&lt;BR/&gt;&lt;BR/&gt;Benefits:
					<xsl:value-of select="Benefits"/>
				</xsl:if>
				<xsl:value-of select="Unit/Footer"/>
			</description>
			<salary>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
				<xsl:choose>
					<xsl:when test="MinSalary &lt; 0">Open</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="MinSalary"/>
					</xsl:otherwise>
				</xsl:choose>
				to
				<xsl:choose>
					<xsl:when test="MaxSalary &lt; 0">Open</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="MaxSalary"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</salary>
			<education>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
				<xsl:value-of select="MinEducation"/>
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</education>
			<jobtype>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
				<xsl:value-of select="Type"/>
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</jobtype>
			<category>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
				<xsl:call-template name="comma-list">
					<xsl:with-param name="value" select="BoardFields/Categories/Value"/>
				</xsl:call-template>
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</category>
			<experience>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
				<xsl:value-of select="MinExperience"/>
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</experience>
		</job>
	</xsl:template>
</xsl:stylesheet>
