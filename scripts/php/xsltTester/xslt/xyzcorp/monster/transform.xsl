<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" omit-xml-declaration="no" indent="yes"/>
    <!-- <xsl:include href="http://xml.postmasterlx.com/common/xsl/StandardBatchTemplates.xsl" />-->

    <!--  Message Field Paramarters -->
    <!--<xsl:param name="action"/>-->
    <xsl:param name="monsterRT.autoRefreshFrequency"/>
    <xsl:param name="monsterRT.bold"/>
    <xsl:param name="monsterRT.CANdesiredDuration"/>
    <xsl:param name="monsterRT.company.name"/>
    <xsl:param name="monsterRT.companyId"/>
    <xsl:param name="monsterRT.companyXCode"/>
    <xsl:param name="monsterRT.customApplyOnlineURL"/>
    <xsl:param name="monsterRT.desiredDuration"/>
    <xsl:param name="monsterRT.display.applylink" select="string('')"/>
    <xsl:param name="monsterRT.display.customfield" select="string('')"/>
    <xsl:param name="monsterRT.hideCompanyName"/>
    <xsl:param name="monster.job.board" select="string('')"/>
    <xsl:param name="monsterRT.password"/>
    <xsl:param name="monsterRT.reply.email"/>
    <xsl:param name="monsterRT.username"/>


    <!--
         <xsl:param name="monsterRT.reply.email" />
          <xsl:param name="monsterRT.sourcecode.front" />
          <xsl:param name="monsterRT.sourcecode.back" />
          <xsl:param name="monsterRT.HHName" />
          <xsl:param name="monsterRT.hidecontactname" />
         -->
    <!--   Standard Mappings   -->


    <xsl:key name="TypeCodeMap" match="Mapping[@name='TypeCode']/Field" use="@value"/>
    <xsl:key name="TypeNameMap" match="Mapping[@name='TypeName']/Field" use="@value"/>
    <xsl:key name="SalaryFrequencyNameMap" match="Mapping[@name='SalaryFrequencyName']/Field" use="@value"/>
    <xsl:key name="SalaryFrequencyCodeMap" match="Mapping[@name='SalaryFrequencyCode']/Field" use="@value"/>
    <xsl:key name="MinExperienceNameMap" match="Mapping[@name='MinExperienceName']/Field" use="@value"/>
    <xsl:key name="MinExperienceCodeMap" match="Mapping[@name='MinExperienceCode']/Field" use="@value"/>
    <xsl:key name="MinEducationNameMap" match="Mapping[@name='MinEducationName']/Field" use="@value"/>
    <xsl:key name="MinEducationCodeMap" match="Mapping[@name='MinEducationCode']/Field" use="@value"/>
    <xsl:key name="RegionMap" match="Mapping[@name='Region']/Field" use="@value"/>
    <xsl:key name="JobTypeNameMap" match="Mapping[@name='JobTypeName']/Field" use="@value"/>
    <xsl:key name="NewJobLevelNameMap" match="Mapping[@name='NewJobLevelName']/Field" use="@value"/>
    <xsl:key name="JobTypeCodeMap" match="Mapping[@name='JobTypeCode']/Field" use="@value"/>
    <xsl:key name="JobLevelNameMap" match="Mapping[@name='JobLevelName']/Field" use="@value"/>
    <xsl:key name="JobLevelCodeMap" match="Mapping[@name='JobLevelCode']/Field" use="@value"/>
    <xsl:key name="CategoryNameMap" match="Mapping[@name='CategoryName']/Field" use="@value"/>
    <xsl:key name="IndustryNameMap" match="Mapping[@name='IndustryName']/Field" use="@value"/>
    <xsl:key name="OccupationNameMap" match="Mapping[@name='OccupationName']/Field" use="@value"/>
    <xsl:key name="CountryMap" match="Mapping[@name='Country']/Field" use="@value"/>

    <!--  Board Mappings  -->
    <xsl:template match="/BatchFeed">
        <xsl:variable name="batchTimestamp" select="//MetaInformation/Timestamp"/>
        <AllJobs>
            <xsl:apply-templates select="Postings/Posting"/>
        </AllJobs>
    </xsl:template>


    <!--  Templates  -->
    <!--<xsl:value-of select='@id'/>-->
    <xsl:template match="Posting">
        <xsl:if test="@action='ADD' or @action='EDIT' ">
            <PostingRequisition>
                <!--<?xml version="1.0" encoding="Windows-1252"?>-->
                <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                    <SOAP-ENV:Header>
                        <mh:MonsterHeader xmlns:mh="http://schemas.monster.com/MonsterHeader">
                            <mh:MessageData>
                                <mh:MessageId>
                                    <xsl:value-of select="@id"/>
                                </mh:MessageId>
                                <mh:Timestamp><xsl:value-of select="//MetaInformation/Timestamp"/>T21:00:00Z</mh:Timestamp>
                            </mh:MessageData>
                            <mh:ProcessingReceiptRequest>
                                <mh:Address transportType="http">http</mh:Address>
                            </mh:ProcessingReceiptRequest>
                        </mh:MonsterHeader>
                        <wsse:Security xmlns:wsse="http://schemas.xmlsoap.org/ws/2002/04/secext">
                            <wsse:UsernameToken>
                                <wsse:Username>
                                    <xsl:value-of select="$monsterRT.username"/>
                                </wsse:Username>
                                <wsse:Password>
                                    <xsl:value-of select="$monsterRT.password"/>
                                </wsse:Password>
                            </wsse:UsernameToken>
                        </wsse:Security>
                    </SOAP-ENV:Header>
                    <SOAP-ENV:Body>
                        <xsl:element name="Job" xmlns="http://schemas.monster.com/Monster"
                                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                            <xsl:attribute name="jobRefCode">
                                <xsl:value-of select="PublicId"/>
                            </xsl:attribute>
                            <xsl:attribute name="jobAction">addOrUpdate</xsl:attribute>
                            <xsl:attribute name="jobComplete">true</xsl:attribute>
                            <xsl:attribute name="enforceUniqueness">true</xsl:attribute>
                            <xsl:attribute name="xsi:schemaLocation">http://schemas.monster.com/Monster
                                http://schemas.monster.com/Current/xsd/Monster.xsd
                            </xsl:attribute>
                            <!--<xsl:attribute name="xmlns">http://schemas.monster.com/Monster</xsl:attribute>-->

                            <RecruiterReference>
                                <UserName>
                                    <xsl:value-of select="$monsterRT.username"/>
                                </UserName>
                            </RecruiterReference>
                            <xsl:element name="CompanyReference">
                                <xsl:attribute name="companyId">
                                    <xsl:value-of select="$monsterRT.companyId"/>
                                </xsl:attribute>
                                <CompanyXCode>
                                    <xsl:value-of select='$monsterRT.companyXCode'/>
                                </CompanyXCode>
                                <CompanyName>
                                    <xsl:value-of select="$monsterRT.company.name"/>
                                </CompanyName>
                            </xsl:element>
                            <Channel monsterId="58">MONS</Channel>
                            <JobInformation>
                                <JobTitle><xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[![CDATA[]]><xsl:value-of select="Title"/><![CDATA[]]]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text></JobTitle>
                                <xsl:element name="JobLevel">
                                    <xsl:choose>
                                        <xsl:when test="BoardFields/Careerlevel/Value">
                                            <xsl:attribute name="monsterId">
                                                <xsl:value-of select="BoardFields/Careerlevel/Value"/>
                                            </xsl:attribute>
                                            <xsl:value-of select="key('NewJobLevelNameMap',BoardFields/Careerlevel/Value)"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:attribute name="monsterId">
                                                <xsl:value-of select="key('JobLevelCodeMap',MinExperience)"/>
                                            </xsl:attribute>
                                            <xsl:value-of select="key('JobLevelNameMap',MinExperience)"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:element>
                                <xsl:element name="JobType">
                                    <xsl:attribute name="monsterId">
                                        <xsl:value-of select="key('JobTypeCodeMap',Duration)"/>
                                    </xsl:attribute>
                                    <xsl:value-of select="key('JobTypeNameMap',Duration)"/>
                                </xsl:element>
                                <xsl:element name="JobStatus">
                                    <xsl:attribute name="monsterId">
                                        <xsl:value-of select="key('TypeCodeMap',Type)"/>
                                    </xsl:attribute>
                                    <xsl:value-of select="key('TypeNameMap',Type)"/>
                                </xsl:element>
                                <!--<xsl:value-of select="Type"/>-->
                                <!--<xsl:element name="JobShift">
                                                                            <xsl:attribute name="monsterId">21</xsl:attribute>JobShiftFirst
                                                                        </xsl:element>-->
                                <xsl:if test="$monster.job.board != ''">
                                    <xsl:element name="ProDiversity">true</xsl:element>
                                </xsl:if>
                                <Salary>
                                    <Currency monsterId="1">USD</Currency>

                                    <xsl:variable name="minSal">
                                        <xsl:choose>
                                            <xsl:when test="string(MinSalary) = '-1'">0</xsl:when>
                                            <xsl:when test="string(MinSalary) = ''">0</xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="MinSalary"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                    <xsl:variable name="maxSal">
                                        <xsl:choose>
                                            <xsl:when test="string(MaxSalary) = '-1'">0</xsl:when>
                                            <xsl:when test="string(MaxSalary) = ''">0</xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="MaxSalary"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>

                                    <xsl:choose>
                                        <xsl:when test="$minSal = 0 and $maxSal = 0">
                                            <xsl:element name="CompensationType">
                                                <xsl:attribute name="monsterId">
                                                    <xsl:value-of select="key('SalaryFrequencyCodeMap',SalaryFrequency)"/>
                                                </xsl:attribute>
                                                <xsl:value-of select="key('SalaryFrequencyNameMap',SalaryFrequency)"/>
                                            </xsl:element>
                                            <SalaryDescription>Commensurate with Experience</SalaryDescription>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <SalaryMin>
                                                <xsl:choose>
                                                    <xsl:when test="string(MinSalary) = '-1'">0</xsl:when>
                                                    <xsl:when test="MinSalary/text()">0</xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="MinSalary"/>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </SalaryMin>
                                            <SalaryMax>
                                                <xsl:choose>
                                                    <xsl:when test="string(MaxSalary) = '-1'">0</xsl:when>
                                                    <xsl:when test="MaxSalary/text()">0</xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="MaxSalary"/>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </SalaryMax>
                                            <xsl:element name="CompensationType">
                                                <xsl:attribute name="monsterId">
                                                    <xsl:value-of select="key('SalaryFrequencyCodeMap',SalaryFrequency)"/>
                                                </xsl:attribute>
                                                <xsl:value-of select="key('SalaryFrequencyNameMap',SalaryFrequency)"/>
                                            </xsl:element>
                                            <SalaryDescription/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </Salary>
                                <Contact hideAll="true" hideStreetAddress="true" hideCity="true"
                                         hideState="true" hidePostalCode="true" hideCountry="true"
                                         hideContactInfoField="true" hideEmailAddress="true"
                                         hideFax="true" hideName="true" hidePhone="true">
                                    <xsl:choose>
                                        <xsl:when test="$monsterRT.hideCompanyName = 'false'">
                                            <xsl:attribute name="hideCompanyName">false</xsl:attribute>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:attribute name="hideCompanyName">true</xsl:attribute>
                                        </xsl:otherwise>
                                    </xsl:choose>

                                    <Name>Recruiter</Name>
                                    <CompanyName>
                                        <xsl:choose>
                                            <xsl:when test="Unit/CustomBoardFields/Companyid/Value/text()">
                                                <xsl:value-of select="Unit/CustomBoardFields/Companyid/Value" />
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="$monsterRT.company.name"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </CompanyName>
                                    <Address>
                                        <StreetAddress>Street Address</StreetAddress>
                                        <StreetAddress2/>
                                        <City>
                                            <xsl:value-of select="Locations/Location/City"/>
                                        </City>
                                        <State>
                                            <xsl:value-of select="key('RegionMap',Locations/Location/Region)"/>
                                        </State>
                                        <CountryCode>
                                            <xsl:value-of select="key('CountryMap',Locations/Location/Country)"/>
                                        </CountryCode>
                                        <PostalCode>
                                            <xsl:value-of select="Locations/Location/PostalCode"/>
                                        </PostalCode>
                                    </Address>
                                    <Phones/>
                                    <E-mail>
                                        <xsl:choose>
                                            <xsl:when test="ApplicationInformation/Email/text()">
                                                <xsl:value-of select="ApplicationInformation/Email"/>
                                            </xsl:when>
                                            <xsl:otherwise>abc@yahoo.com</xsl:otherwise>
                                        </xsl:choose>
                                    </E-mail>
                                    <WebSite>
                                        <xsl:value-of select="ApplicationInformation/URL"/>
                                    </WebSite>
                                </Contact>
                                <xsl:choose>
                                    <xsl:when test="$monsterRT.display.applylink != ''">
                                        <DisableApplyOnline>1</DisableApplyOnline>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <DisableApplyOnline>0</DisableApplyOnline>
                                    </xsl:otherwise>
                                </xsl:choose>


                                <HideCompanyInfo>0</HideCompanyInfo>
                                <JobBody><xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[![CDATA[]]>
                                    <xsl:value-of select="Unit/Header" disable-output-escaping="yes"/>
                                    <xsl:value-of select="Description" disable-output-escaping="yes"/>
                                    <xsl:text>
                                    </xsl:text>
                                    <xsl:if test="Qualifications/text()">
                                        <br></br>
                                        <b>Qualifications</b>
                                        <br></br>
                                        <xsl:value-of select="Qualifications" disable-output-escaping="yes"/>
                                    </xsl:if>
                                    <xsl:if test="Benefits/text()">
                                        <br></br>
                                        <b>Benefits</b>
                                        <br></br>
                                        <xsl:value-of select="Benefits" disable-output-escaping="yes"/>
                                    </xsl:if>

                                    <xsl:text>
                                    </xsl:text>
                                    <xsl:value-of select="Unit/Footer" disable-output-escaping="yes"/>
                                    <xsl:text>
                                    </xsl:text><![CDATA[]]]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
                                </JobBody>
                                <AdditionalSearchKeywords>
                                    <xsl:value-of select="SkillsRequired"/>
                                </AdditionalSearchKeywords>

                                <xsl:if test="$monsterRT.display.customfield != '' ">
                                    <CustomJobFields>
                                        <CustomJobField>
                                            <CJFId>
                                                <xsl:value-of select="$monsterRT.display.customfield"/>
                                            </CJFId>
                                            <CJFValue>
                                                <xsl:value-of select="@id"/>
                                            </CJFValue>
                                        </CustomJobField>
                                    </CustomJobFields>
                                </xsl:if>
                               <!--<StartDate/>-->
                                <xsl:element name="EducationLevel">
                                    <xsl:attribute name="monsterId">
                                        <xsl:value-of select="key('MinEducationCodeMap',MinEducation)"/>
                                    </xsl:attribute>
                                    <xsl:value-of select="key('MinEducationNameMap',MinEducation)"/>
                                </xsl:element>
                                <!--<Duties/>-->
                                <!--<EmployeeReferralInformation/>-->
                                <!--<AcceptReferrals>true</AcceptReferrals>
                                                            <BonusAmount>1000</BonusAmount>
                                                            <BonusCurrency monsterId="1">USD</BonusCurrency>
                                                        </EmployeeReferralInformation>-->
                                <xsl:if test="MinExperience != 'NOT_SPECIFIED' and MinExperience != ''">
                                    <xsl:element name="YearsOfExperience">
                                        <xsl:attribute name="monsterId">
                                            <xsl:value-of select="key('MinExperienceCodeMap',MinExperience)"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="key('MinExperienceNameMap',MinExperience)"/>
                                    </xsl:element>
                                </xsl:if>

                               <!-- <xsl:if test="$monsterRT.customApplyOnlineURL != ''">
                                    <xsl:element name="CustomApplyOnlineURL">
                                        <xsl:value-of select="ApplicationInformation/URL"/>
                                    </xsl:element>
                                </xsl:if>-->
                                  <CustomApplyOnlineURL><xsl:value-of select="ApplicationInformation/URL"/></CustomApplyOnlineURL>
                            </JobInformation>

                            <JobPostings>
                                <JobPosting jobPostingAction="add">
                                    <xsl:if test="$monsterRT.desiredDuration != ''">
                                        <xsl:attribute name="desiredDuration">
                                            <xsl:value-of select="$monsterRT.desiredDuration"/>
                                        </xsl:attribute>
                                    </xsl:if>
                                    <xsl:if test="$monsterRT.bold != ''">
                                        <xsl:attribute name="bold">
                                            <xsl:value-of select="$monsterRT.bold"/>
                                        </xsl:attribute>
                                    </xsl:if>
                                    <InventoryPreference>
                                        <xsl:if test="$monsterRT.autoRefreshFrequency != ''">
                                        <Autorefresh desired="true">
                                            <Frequency><xsl:value-of select="$monsterRT.autoRefreshFrequency"/></Frequency>
                                        </Autorefresh>
                                        </xsl:if>
                                        <xsl:if test="$monsterRT.CANdesiredDuration != ''">
                                        <CareerAdNetwork desired="true">
                                            <Duration><xsl:value-of select="$monsterRT.CANdesiredDuration"/></Duration>
                                        </CareerAdNetwork>
                                        </xsl:if>
                                    </InventoryPreference>
                                    <RecruiterReference>
                                        <UserName>
                                            <xsl:value-of select="$monsterRT.username"/>
                                        </UserName>
                                    </RecruiterReference>
                                    <Location>
                                        <City>
                                            <xsl:value-of select="Locations/Location/City"/>
                                        </City>
                                        <State>
                                            <xsl:value-of select="key('RegionMap',Locations/Location/Region)"/>
                                        </State>
                                        <CountryCode>
                                            <xsl:value-of select="key('CountryMap',Locations/Location/Country)"/>
                                        </CountryCode>
                                        <PostalCode>
                                            <xsl:value-of select="Locations/Location/PostalCode"/>
                                        </PostalCode>
                                        <Continent>NA</Continent>
                                    </Location>
                                    <xsl:element name="JobCategory">
                                        <xsl:attribute name="monsterId">
                                            <xsl:value-of select="BoardFields/Category"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="key('CategoryNameMap',BoardFields/Category)"/>
                                    </xsl:element>
                                    <xsl:if test="BoardFields/Occupation/Value[1]">
                                        <xsl:element name="JobOccupations">
                                            <xsl:for-each select="BoardFields/Occupation/Value">
                                                <xsl:element name="JobOccupation">
                                                    <xsl:attribute name="monsterId">
                                                        <xsl:value-of select="."/>
                                                        <!--BoardFields/Occupation/Value" />-->
                                                    </xsl:attribute>
                                                    <!--<xsl:attribute name="categoryId">
                                                                                                                      <xsl:value-of select="../../../BoardFields/Category/Value" />
                                                                                                                  </xsl:attribute>-->
                                                    <xsl:element name="Name">
                                                        <xsl:value-of select="key('OccupationNameMap',concat(BoardFields/Category/Value,'-',.))"/>
                                                    </xsl:element>
                                                </xsl:element>
                                            </xsl:for-each>
                                        </xsl:element>
                                    </xsl:if>
                                    <JobCity/>
                                    <xsl:if test="$monster.job.board ='' ">
                                        <BoardName monsterId="1">Monster</BoardName>
                                    </xsl:if>
                                    <xsl:if test="$monster.job.board !='' ">
                                        <BoardName>
                                            <xsl:attribute name="monsterId">
                                                <xsl:value-of select="$monster.job.board"/>
                                            </xsl:attribute>
                                            Monster
                                        </BoardName>
                                    </xsl:if>
                                    <!--<PhysicalAddress>
                                        <City>
                                            <xsl:value-of select="Locations/Location/City"/>
                                        </City>
                                        <State>
                                            <xsl:value-of select="key('RegionMap',Locations/Location/Region)"/>
                                        </State>
                                        <CountryCode>
                                            <xsl:value-of select="key('CountryMap',Locations/Location/Country)"/>
                                        </CountryCode>
                                        <PostalCode>
                                            <xsl:value-of select="Locations/Location/PostalCode"/>
                                        </PostalCode>
                                    </PhysicalAddress>-->
                                    <xsl:if test="Unit/CustomBoardFields/Templateid/Value/text()">
                                        <DisplayTemplate>
                                            <xsl:attribute name="monsterId">
                                                <xsl:value-of select="Unit/CustomBoardFields/Templateid/Value"/>
                                            </xsl:attribute>
                                        </DisplayTemplate>
                                    </xsl:if>
                                    <xsl:if test="BoardFields/Industry">
                                        <xsl:element name="Industries">
                                            <xsl:for-each select="BoardFields/Industry/Value">
                                                <xsl:element name="Industry">
                                                    <xsl:element name="IndustryName">
                                                        <xsl:attribute name="monsterId">
                                                            <xsl:value-of select="."/>
                                                        </xsl:attribute>
                                                        <xsl:value-of select="key('IndustryNameMap',.)"/>
                                                    </xsl:element>
                                                </xsl:element>
                                            </xsl:for-each>
                                        </xsl:element>
                                    </xsl:if>

                                    <!--<FilterParameters/>-->
                                </JobPosting>
                            </JobPostings>

                        </xsl:element>
                    </SOAP-ENV:Body>
                </SOAP-ENV:Envelope>
            </PostingRequisition>
        </xsl:if>

        <xsl:if test="@action = 'REMOVE' ">
            <PostingRequisition>
                <!--<?xml version="1.0" encoding="UTF-8"?>-->
                <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                    <SOAP-ENV:Header>
                        <mh:MonsterHeader xmlns:mh="http://schemas.monster.com/MonsterHeader">
                            <mh:MessageData>
                                <mh:MessageId>
                                    <xsl:value-of select="@id"/>
                                </mh:MessageId>
                                <mh:Timestamp><xsl:value-of select="//MetaInformation/Timestamp"/>T21:00:00Z</mh:Timestamp>
                            </mh:MessageData>
                            <mh:ProcessingReceiptRequest>
                                <mh:Address transportType="http">http</mh:Address>
                            </mh:ProcessingReceiptRequest>
                        </mh:MonsterHeader>
                        <wsse:Security xmlns:wsse="http://schemas.xmlsoap.org/ws/2002/04/secext">
                            <wsse:UsernameToken>
                                <wsse:Username>
                                    <xsl:value-of select="$monsterRT.username"/>
                                </wsse:Username>
                                <wsse:Password>
                                    <xsl:value-of select="$monsterRT.password"/>
                                </wsse:Password>
                            </wsse:UsernameToken>
                        </wsse:Security>
                    </SOAP-ENV:Header>
                    <SOAP-ENV:Body>
                        <Monster:Delete xmlns:Monster="http://schemas.monster.com/Monster"
                                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                        xsi:schemaLocation="http://schemas.monster.com/Monster http://schemas.monster.com/Current/xsd/Monster.xsd">
                            <Monster:Target>JobPosting</Monster:Target>
                            <Monster:DeleteBy>
                                <Monster:Criteria>RecruiterName</Monster:Criteria>
                                <Monster:Value>
                                    <xsl:value-of select="$monsterRT.username"/>
                                </Monster:Value>
                            </Monster:DeleteBy>
                            <Monster:DeleteBy>
                                <Monster:Criteria>RefCode</Monster:Criteria>
                                <Monster:Value>
                                    <xsl:value-of select="PublicId"/>
                                </Monster:Value>
                            </Monster:DeleteBy>
                        </Monster:Delete>
                    </SOAP-ENV:Body>
                </SOAP-ENV:Envelope>
            </PostingRequisition>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
