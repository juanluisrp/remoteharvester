<!--

    (c) 2020 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gmx="http://www.isotc211.org/2005/gmx"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:gml="http://www.opengis.net/gml/3.2"
                xmlns:gml31="http://www.opengis.net/gml"
                xmlns:gn-fn-index="http://geonetwork-opensource.org/xsl/functions/index"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:include href="constant.xsl"/>
  <xsl:include href="iso19139-utility.xsl"/>

  <xsl:variable name="properties" as="node()*">
    <property name="metadataIdentifier" xpath="gmd:fileIdentifier"/>
    <property name="parentUuid" xpath="gmd:parentIdentifier" />
    <property name="standardName" xpath="gmd:metadataStandardName" type="multilingual" />
    <property name="standardVersion" xpath="gmd:metadataStandardVersion" type="multilingual" />
    <property name="dateStamp" xpath="gmd:dateStamp" type="date" />
    <property name="" xpath="gmd:contact" type="contact"/>
    <property name="tagNumber" xpath="count(//gmd:keyword)"/>
    <property name="resourceTitle" xpath="gmd:identificationInfo/*/gmd:citation/*/gmd:title" type="multilingual"/>
    <property name="resourceAltTitle" xpath="gmd:identificationInfo/*/gmd:citation/*/gmd:resourceAltTitle" type="multilingual"/>
    <property name="resourceAbstract" xpath="gmd:identificationInfo/*/gmd:abstract" type="multilingual"/>
    <property name="codelist" xpath="//*[(*/@codeListValue != '' and name(*) != 'gmd:CI_RoleCode' and name(*) != 'gmd:CI_DateTypeCode' and name(*) != 'gmd:LanguageCode') or gmd:MD_TopicCategoryCode]" type="codelist" />
    <!-- and name(*) != 'gmd:CI_RoleCode' and name(*) != 'gmd:CI_DateTypeCode' and name(*) != 'gmd:MD_CharacterSetCode' and name(*) != 'gmd:LanguageCode' -->
    <!--<property name="topic" xpath="gmd:identificationInfo/*/gmd:topicCategory" type="codelist"  forceName="true"/>
    <property name="codelist" xpath="//*[(*/@codeListValue != '') and name(*) != 'gmd:CI_RoleCode' and name(*) != 'gmd:CI_DateTypeCode' and name(*) != 'gmd:MD_CharacterSetCode' and name(*) != 'gmd:LanguageCode']" type="codelist" />-->
    
    <!--<property name="characterSet" xpath="gmd:characterSet[*/@codeListValue != '']" type="codelist" forceName="true" />
    <property name="resourceCharacterSet" xpath="gmd:identificationInfo/*/gmd:characterSet[*/@codeListValue != '']" type="codelist" forceName="true" />-->
    
    <property name="resourceEdition" xpath="gmd:identificationInfo/*/gmd:edition" />
    <property name="resourceCredit" xpath="gmd:identificationInfo/*/gmd:credit" type="multilingual" />  
    <property name="ForResource" xpath="gmd:identificationInfo/*/gmd:pointOfContact" type="contact"/>     
    <property name="resolutionScaleDenominator" xpath="gmd:identificationInfo/*/gmd:spatialResolution/gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator/gco:Integer[. castable as xs:decimal]" type="integer"/>

    <!--<property name="serviceType" xpath="gmd:identificationInfo/*/srv:serviceType/gco:LocalName" />-->
    
    <property name="format" xpath="gmd:distributionInfo/*/gmd:distributionFormat/*/gmd:name" />
    <property name="ForDistribution" xpath="gmd:distributionInfo/*/gmd:distributor/*/gmd:distributorContact" type="contact" />
    <property name="license" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:otherConstraints" type="multilingual" />
    <property name="MD_LegalConstraintsOtherConstraints" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:otherConstraints" type="multilingual" />
    <property name="MD_ConstraintsOtherConstraints" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_Constraints/gmd:otherConstraints" type="multilingual" />
    <property name="MD_SecurityConstraintsOtherConstraints" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_SecurityConstraints/gmd:otherConstraints" type="multilingual" />
    
    <property name="MD_LegalConstraintsUseLimitation" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:useLimitation" type="multilingual" />
    <property name="MD_ConstraintsUseLimitation" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_Constraints/gmd:useLimitation" type="multilingual" />
    <property name="MD_SecurityConstraintsUseLimitation" xpath="gmd:identificationInfo/*/gmd:resourceConstraints/gmd:MD_SecurityConstraints/gmd:useLimitation" type="multilingual" />
    
  </xsl:variable>


  <xsl:variable name="languages" as="node()*">
    <xsl:call-template name="get-languages">
      <xsl:with-param name="metadata" select="gmd:MD_Metadata"/>
    </xsl:call-template>
  </xsl:variable>
  
  <xsl:variable name="mainLanguage" select="$languages[@id = 'default']/@code" />
  
  
  
  <xsl:template match="/">
    <indexRecords>
      <xsl:apply-templates mode="index" select="*"/>
    </indexRecords>
  </xsl:template>


  <xsl:template mode="index"
                match="indexRecord">
    <xsl:copy>
      <xsl:copy-of select="*[name() != 'document']"/>
      <xsl:variable name="xml"
                    select="parse-xml(document)"/>
      <xsl:apply-templates mode="index"
                           select="$xml"/>
    </xsl:copy>
  </xsl:template>


  <!-- Contact -->
  <xsl:template mode="index"
                match="gmd:contact|gmd:pointOfContact|gmd:distributorContact">
    <xsl:param name="fieldSuffix" />
    
    <xsl:variable name="organisationName"
      select="*[1]/gmd:organisationName[1]/(gco:CharacterString|gmx:Anchor)"
      as="xs:string*"/>
    <xsl:variable name="uuid" select="@uuid"/>
    
    <xsl:variable name="role"
      select="replace(*[1]/gmd:role/*/@codeListValue, ' ', '')"
      as="xs:string?"/>
    <xsl:variable name="logo" select=".//gmx:FileName/@src"/>
    <xsl:variable name="website" select=".//gmd:onlineResource/*/gmd:linkage/gmd:URL"/>
    <xsl:variable name="email"
      select="*[1]/gmd:contactInfo/*/gmd:address/*/gmd:electronicMailAddress/gco:CharacterString"/>
    <xsl:variable name="phone"
      select="*[1]/gmd:contactInfo/*/gmd:phone/*/gmd:voice[normalize-space(.) != '']/*/text()"/>
    <xsl:variable name="individualName"
      select="*[1]/gmd:individualName/gco:CharacterString/text()"/>
    <xsl:variable name="positionName"
      select="*[1]/gmd:positionName/gco:CharacterString/text()"/>
    <xsl:variable name="address" select="string-join(*[1]/gmd:contactInfo/*/gmd:address/*/(
      gmd:deliveryPoint|gmd:postalCode|gmd:city|
      gmd:administrativeArea|gmd:country)/gco:CharacterString/text(), ', ')"/>
    <xsl:if test="normalize-space($organisationName) != ''">
      <xsl:element name="Org{$fieldSuffix}">
        <xsl:value-of select="$organisationName"/>
      </xsl:element>
      <xsl:element name="{replace($role, '[^a-zA-Z0-9-]', '')}Org{$fieldSuffix}">
        <xsl:value-of select="$organisationName"/>
      </xsl:element>
    </xsl:if>
    
    <xsl:element name="contact{$fieldSuffix}">
      <!-- TODO: Can be multilingual -->
      <organisation><xsl:value-of select="$organisationName"/></organisation>
      <role><xsl:value-of select="$role"/></role>
      <email><xsl:value-of select="$email[1]"/></email>
      <website><xsl:value-of select="$website"/></website>
      <logo><xsl:value-of select="$logo"/></logo>
      <individual><xsl:value-of select="$individualName"/></individual>
      <position><xsl:value-of select="$positionName"/></position>
      <phone><xsl:value-of select="$phone[1]"/></phone>
      <address><xsl:value-of select="$address"/></address>
    </xsl:element> 
  </xsl:template>


  <!-- Resource identifier -->
  <xsl:template mode="index"
                match="gmd:identifier">
    <xsl:for-each select="*">
      <resourceIdentifier>
        <code><xsl:value-of select="gmd:code/(gco:CharacterString|gmx:Anchor)"/></code>
        <codeSpace><xsl:value-of select="gmd:codeSpace/(gco:CharacterString|gmx:Anchor)"/></codeSpace>
        <link><xsl:value-of select="gmd:code/gmx:Anchor/@xlink:href"/></link>
       </resourceIdentifier>
    </xsl:for-each>
  </xsl:template>
 
 
  <!-- Reference system information -->
  <xsl:template mode="index"
                match="gmd:referenceSystemInfo">
    <xsl:for-each select="gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier">
      <xsl:variable name="crs" select="gmd:code/*/text()"/>
      
      <xsl:if test="$crs != ''">
        <coordinateSystem>
          <xsl:value-of select="$crs"/>
        </coordinateSystem>
      </xsl:if>
      
      <crsDetails>
        <code><xsl:value-of select="(gmd:code/*/text())[1]"/></code>
        <codeSpace><xsl:value-of select="gmd:codeSpace/*/text()"/></codeSpace>
        <name><xsl:value-of select="gmd:code/*/@xlink:title"/></name>
        <url><xsl:value-of select="gmd:code/*/@xlink:href"/></url>
      </crsDetails>
    </xsl:for-each>
  </xsl:template>
  
 
  <!-- Keywords -->
  <xsl:template mode="index"
    match="gmd:descriptiveKeywords" >
    
    <xsl:for-each
      select="gmd:MD_Keywords[gmd:thesaurusName]">
      
      <xsl:variable name="thesaurusName"
        select="gmd:thesaurusName[1]/gmd:CI_Citation/
        gmd:title[1]/(gco:CharacterString|gmx:Anchor)"/>
      
      <xsl:variable name="thesaurusId"
        select="normalize-space(gmd:thesaurusName/gmd:CI_Citation/
        gmd:identifier[position() = 1]/gmd:MD_Identifier/
        gmd:code/(gco:CharacterString|gmx:Anchor)/text())"/>
      
      <xsl:variable name="key">
        <xsl:choose>
          <xsl:when test="$thesaurusId != ''">
            <xsl:value-of select="tokenize($thesaurusId, '\.')[last()]"/>
          </xsl:when>
          <!-- Try to build a thesaurus key based on the name
              by removing space - to be improved. -->
          <xsl:when test="normalize-space($thesaurusName) != ''">
            <xsl:value-of select="replace($thesaurusName, ' ', '-')"/>
          </xsl:when>
        </xsl:choose>
      </xsl:variable>
      
      <xsl:if test="normalize-space($key) != ''">
        <xsl:variable name="keywords"
          select="gmd:keyword[*/normalize-space() != '']"/>
        
        <xsl:call-template name="build-thesaurus-fields">
          <xsl:with-param name="thesaurus" select="$key"/>
          <xsl:with-param name="thesaurusId" select="$thesaurusId"/>
          <xsl:with-param name="keywords" select="$keywords"/>
          <xsl:with-param name="mainLanguage" select="$mainLanguage"/>
          <xsl:with-param name="allLanguages" select="$languages"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:for-each>
    
  </xsl:template>
  
  
  <!-- All keywords element -->
  <xsl:template match="*" mode="index-allkeywords">
    <allKeywords type="object">{
      <xsl:for-each-group select="*/gmd:MD_Keywords"
        group-by="gmd:thesaurusName/*/gmd:title/*/text()">
        <xsl:sort select="current-grouping-key()"/>
        <xsl:variable name="thesaurusName"
          select="current-grouping-key()"/>
        
        <xsl:variable name="thesaurusId"
          select="normalize-space(gmd:thesaurusName/gmd:CI_Citation/
          gmd:identifier[position() = 1]/gmd:MD_Identifier/
          gmd:code/(gco:CharacterString|gmx:Anchor)/text())"/>
        
        <xsl:variable name="key">
          <xsl:choose>
            <xsl:when test="$thesaurusId != ''">
              <xsl:value-of select="$thesaurusId"/>
            </xsl:when>
            <!-- Try to build a thesaurus key based on the name
                by removing space - to be improved. -->
            <xsl:when test="normalize-space($thesaurusName) != ''">
              <xsl:value-of select="replace($thesaurusName, ' ', '-')"/>
            </xsl:when>
          </xsl:choose>
        </xsl:variable>
        
        <xsl:if test="normalize-space($key) != ''">
          <xsl:variable name="thesaurusField"
            select="replace($key, '[^a-zA-Z0-9]', '')"/>
          
          "<xsl:value-of select="$thesaurusField"/>": {
          "id": "<xsl:value-of select="gn-fn-index:json-escape($thesaurusId)"/>",
          "title": "<xsl:value-of select="gn-fn-index:json-escape($thesaurusName)"/>",
          "theme": "<xsl:value-of select="gn-fn-index:json-escape(gmd:type/*/@codeListValue)"/>",
          "link": "<xsl:value-of select="gn-fn-index:json-escape(@xlink:href)"/>",
          "keywords": [
          <xsl:for-each select="gmd:keyword[*/normalize-space() != '']">
            <xsl:value-of select="gn-fn-index:add-multilingual-field('keyword', ., $languages)/text()"/>
            <xsl:if test="position() != last()">,</xsl:if>
          </xsl:for-each>
          ]}
          <xsl:if test="position() != last()">,</xsl:if>
        </xsl:if>
      </xsl:for-each-group>
      
      
      <xsl:variable name="keywordWithNoThesaurus"
        select="//gmd:MD_Keywords[
        not(gmd:thesaurusName)
        or gmd:thesaurusName/*/gmd:title/*/text() = '']"/>
      <xsl:variable name="hasKeywordWithThesaurus"
        select="count(*/gmd:MD_Keywords[
        gmd:thesaurusName/*/gmd:title/*/text() != '']) > 0"/>
      
      <xsl:if test="$hasKeywordWithThesaurus and $keywordWithNoThesaurus">,</xsl:if>
      
      <xsl:variable name="types">
        <xsl:for-each select="distinct-values($keywordWithNoThesaurus//gmd:type/*/@codeListValue[. != ''])">
          <type><xsl:value-of select="."/></type>
        </xsl:for-each>
        <xsl:if test="count($keywordWithNoThesaurus[not(gmd:type) or gmd:type/*/@codeListValue = '']) > 0">
          <type></type>
        </xsl:if>
      </xsl:variable>
      
      <xsl:for-each select="$types/*">
        <xsl:variable name="thesaurusType"
          select="."/>
        
        <xsl:variable name="thesaurusField"
          select="concat('otherKeywords-', $thesaurusType)"/>
        "<xsl:value-of select="$thesaurusField"/>": {
        "keywords": [
        <xsl:for-each select="$keywordWithNoThesaurus
          [if ($thesaurusType = '') then not(gmd:type) or gmd:type/*/@codeListValue = '' else gmd:type/*/@codeListValue = $thesaurusType]
          /gmd:keyword[*/normalize-space() != '']">
          <xsl:value-of select="gn-fn-index:add-multilingual-field('keyword', ., $languages)/text()"/>
          <xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>
        ]}
        <xsl:if test="position() != last()">,</xsl:if>
      </xsl:for-each>
      }</allKeywords>
  </xsl:template>
  
  
  <!-- Identification info -->
  <xsl:template match="gmd:identificationInfo/*" mode="index">
    <xsl:param name="isDataset" />
    
    <xsl:for-each select="gmd:language/(gco:CharacterString|gmd:LanguageCode/@codeListValue)">
      <resourceLanguage>
        <xsl:value-of select="."/>
      </resourceLanguage>
    </xsl:for-each>
    
    <xsl:apply-templates select="gmd:citation/*/gmd:identifier" mode="index"/>
    
    <xsl:variable name="keywords"
      select=".//gmd:keyword[*/normalize-space() != '']"/>
    
    <xsl:variable name="keywordTypes"
      select="distinct-values(.//gmd:descriptiveKeywords/*/
      gmd:type/*/@codeListValue[. != ''])"/>
    <xsl:variable name="geoDesciption"
      select="//gmd:geographicElement/gmd:EX_GeographicDescription/
      gmd:geographicIdentifier/gmd:MD_Identifier/
      gmd:code[*/normalize-space(.) != '']
      |//gmd:EX_Extent/gmd:description[*/normalize-space(.) != '']"/>
    
    <xsl:for-each select="$keywordTypes">
      <xsl:variable name="type"
        select="."/>
      <xsl:variable name="keywordsForType"
        select="$keywords[../gmd:type/*/@codeListValue = $type]
        |$geoDesciption[$type = 'place']"/>
      <xsl:element name="keywordType-{$type}">
        <xsl:attribute name="type" select="'object'"/>
        [<xsl:for-each select="$keywordsForType">
          <xsl:value-of select="gn-fn-index:add-multilingual-field('keyword', ., $languages)/text()"/>
          <xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>]
      </xsl:element>
    </xsl:for-each>
    
    <xsl:apply-templates select="gmd:descriptiveKeywords" mode="index"/>
    
    <xsl:variable name="keywords"
      select=".//gmd:keyword[*/normalize-space() != '']"/>
    <xsl:variable name="keywords"
      select=".//gmd:keyword[*/normalize-space() != '']"/>
    
    <xsl:if test="count($keywords) > 0">
      <xsl:copy-of select="gn-fn-index:add-multilingual-field('tag', $keywords, $languages)"/>
    </xsl:if>
    
    <xsl:apply-templates select="." mode="index-allkeywords"/>
    
    <xsl:variable name="isMapDigital"
      select="count(gmd:identificationInfo/*/gmd:citation/*/gmd:presentationForm[*/@codeListValue = 'mapDigital']) > 0"/>
    <xsl:variable name="isStatic"
      select="count(gmd:distributionInfo/*/gmd:distributionFormat/*/gmd:name/*[contains(., 'PDF') or contains(., 'PNG') or contains(., 'JPEG')]) > 0"/>
    <xsl:variable name="isInteractive"
      select="count(gmd:distributionInfo/*/gmd:distributionFormat/*/gmd:name/*[contains(., 'OGC:WMC') or contains(., 'OGC:OWS-C')]) > 0"/>
    <xsl:variable name="isPublishedWithWMCProtocol"
      select="count(gmd:distributionInfo/*/gmd:transferOptions/*/gmd:onLine/*/gmd:protocol[starts-with(gco:CharacterString, 'OGC:WMC')]) > 0"/>
    
    <xsl:choose>
      <xsl:when test="$isDataset and $isMapDigital and
        ($isStatic or $isInteractive or $isPublishedWithWMCProtocol)">
        <resourceType>map</resourceType>
        <xsl:choose>
          <xsl:when test="$isStatic">
            <resourceType>map/static</resourceType>
          </xsl:when>
          <xsl:when test="$isInteractive or $isPublishedWithWMCProtocol">
            <resourceType>map/interactive</resourceType>
          </xsl:when>
        </xsl:choose>
      </xsl:when>
    </xsl:choose>
    
    <xsl:for-each select="gmd:citation/*/gmd:date/gmd:CI_Date">
      <xsl:variable name="dateType"
        select="gmd:dateType[1]/gmd:CI_DateTypeCode/@codeListValue"
        as="xs:string?"/>
      <xsl:variable name="date"
        select="string(gmd:date[1]/gco:Date|gmd:date[1]/gco:DateTime)"/>
      
      <xsl:variable name="zuluDateTime" as="xs:string?">
        <xsl:value-of select="$date"/>
      </xsl:variable>
      
      <xsl:choose>
        <xsl:when test="$zuluDateTime != ''">
          <xsl:element name="{$dateType}DateForResource">
            <xsl:value-of select="$zuluDateTime"/>
          </xsl:element>
          <xsl:element name="{$dateType}YearForResource">
            <xsl:value-of select="substring($zuluDateTime, 0, 5)"/>
          </xsl:element>
          <xsl:element name="{$dateType}MonthForResource">
            <xsl:value-of select="substring($zuluDateTime, 0, 8)"/>
          </xsl:element>
        </xsl:when>
        <xsl:otherwise>
          <indexingErrorMsg>Warning / Date <xsl:value-of select="$dateType"/> with value '<xsl:value-of select="$date"/>' was not a valid date format.</indexingErrorMsg>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
    
    <xsl:for-each select="gmd:citation/*/gmd:date/gmd:CI_Date">
      <xsl:variable name="dateType"
        select="gmd:dateType[1]/gmd:CI_DateTypeCode/@codeListValue"
        as="xs:string?"/>
      <xsl:variable name="date"
        select="string(gmd:date[1]/gco:Date|gmd:date[1]/gco:DateTime)"/>
      
      <xsl:variable name="zuluDate"
        select="$date"/>
      <xsl:if test="$zuluDate != ''">
        <resourceDate>
          <type><xsl:value-of select="$dateType"/></type>
          <date><xsl:value-of select="$zuluDate"/></date>     
        </resourceDate>
      </xsl:if>
    </xsl:for-each>
    
    
    <xsl:for-each-group select="gmd:citation/*/gmd:date/gmd:CI_Date/gmd:date/*/text()"
      group-by=".">
      <xsl:variable name="zuluDate"
        select="."/>
      <xsl:if test="$zuluDate != ''">
        <resourceTemporalDateRange>
          <gte><xsl:value-of select="$zuluDate"/></gte>
          <lte><xsl:value-of select="$zuluDate"/></lte>
      </resourceTemporalDateRange>
      </xsl:if>
    </xsl:for-each-group>
    
    <!-- TODO -->
    <!-- TODO can be multilingual desc and name -->
    <!-- 
    <xsl:variable name="overviews"
                    select="gmd:graphicOverview/gmd:MD_BrowseGraphic/
                            gmd:fileName/gco:CharacterString[. != '']"/>
      <xsl:copy-of select="gn-fn-index:add-field('hasOverview', if (count($overviews) > 0) then 'true' else 'false')"/>
      
      
      <xsl:for-each select="$overviews">
        
        <overview type="object">{
          "url": "<xsl:value-of select="normalize-space(.)"/>"
          <xsl:if test="$isStoringOverviewInIndex">
            <xsl:variable name="data"
                          select="util:buildDataUrl(., 140)"/>
            <xsl:if test="$data != ''">,
              "data": "<xsl:value-of select="$data"/>"
            </xsl:if>
          </xsl:if>
          <xsl:if test="normalize-space(../../gmd:fileDescription) != ''">,
            "text": <xsl:value-of select="gn-fn-index:add-multilingual-field('name', ../../gmd:fileDescription, $allLanguages, true())"/>
          </xsl:if>
        }</overview>
      </xsl:for-each>
    -->
    
    <xsl:for-each select="gmd:distance/gco:Distance[. != '']">
      <resolutionDistance>
        <xsl:value-of select="concat(., ' ', @uom)"/>
      </resolutionDistance>
    </xsl:for-each>
    
    
    <!-- TODO -->
    <!--<xsl:for-each select="*/gmd:EX_Extent/*/gmd:EX_BoundingPolygon/gmd:polygon">
      <xsl:variable name="geojson"
        select="util:gmlToGeoJson(
        saxon:serialize((gml:*|gml31:*), 'default-serialize-mode'),
        true(), 5)"/>
      <xsl:choose>
        <xsl:when test="$geojson = ''"></xsl:when>
        <xsl:when test="matches($geojson, '(Error|Warning):.*')">
          <shapeParsingError><xsl:value-of select="$geojson"/></shapeParsingError>
        </xsl:when>
        <xsl:otherwise>
          <shape type="object">
            <xsl:value-of select="$geojson"/>
          </shape>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>-->
    
    <xsl:for-each select="*/gmd:EX_Extent">
      <xsl:copy-of select="gn-fn-index:add-multilingual-field('extentDescription', gmd:description, $languages)"/>
      
      <!-- TODO: index bounding polygon -->
      <xsl:variable name="bboxes"
        select=".//gmd:EX_GeographicBoundingBox[
        ./gmd:westBoundLongitude/gco:Decimal castable as xs:decimal and
        ./gmd:eastBoundLongitude/gco:Decimal castable as xs:decimal and
        ./gmd:northBoundLatitude/gco:Decimal castable as xs:decimal and
        ./gmd:southBoundLatitude/gco:Decimal castable as xs:decimal
        ]"/>
      <xsl:for-each select="$bboxes">
        <xsl:variable name="format" select="'#0.000000'"></xsl:variable>
        
        <xsl:variable name="w"
          select="format-number(./gmd:westBoundLongitude/gco:Decimal/text(), $format)"/>
        <xsl:variable name="e"
          select="format-number(./gmd:eastBoundLongitude/gco:Decimal/text(), $format)"/>
        <xsl:variable name="n"
          select="format-number(./gmd:northBoundLatitude/gco:Decimal/text(), $format)"/>
        <xsl:variable name="s"
          select="format-number(./gmd:southBoundLatitude/gco:Decimal/text(), $format)"/>
        
        <!-- Example: ENVELOPE(-10, 20, 15, 10) which is minX, maxX, maxY, minY order
            http://wiki.apache.org/solr/SolrAdaptersForLuceneSpatial4
            https://cwiki.apache.org/confluence/display/solr/Spatial+Search
            
            bbox field type limited to one. TODO
            <xsl:if test="position() = 1">
              <bbox>
                <xsl:text>ENVELOPE(</xsl:text>
                <xsl:value-of select="$w"/>
                <xsl:text>,</xsl:text>
                <xsl:value-of select="$e"/>
                <xsl:text>,</xsl:text>
                <xsl:value-of select="$n"/>
                <xsl:text>,</xsl:text>
                <xsl:value-of select="$s"/>
                <xsl:text>)</xsl:text>
              </field>
            </xsl:if>
            -->
        <xsl:choose>
          <xsl:when test="-180 &lt;= number($e) and number($e) &lt;= 180 and
            -180 &lt;= number($w) and number($w) &lt;= 180 and
            -90 &lt;= number($s) and number($s) &lt;= 90 and
            -90 &lt;= number($n) and number($n) &lt;= 90">
            <xsl:choose>
              <xsl:when test="$e = $w and $s = $n">
                <location><xsl:value-of select="concat($s, ',', $w)"/></location>
              </xsl:when>
              <xsl:when
                test="($e = $w and $s != $n) or ($e != $w and $s = $n)">
                <!-- Probably an invalid bbox indexing a point only -->
                <location><xsl:value-of select="concat($s, ',', $w)"/></location>
              </xsl:when>
              <xsl:otherwise>
                <geom type="object">
                  <xsl:text>{"type": "Polygon",</xsl:text>
                  <xsl:text>"coordinates": [[</xsl:text>
                  <xsl:value-of select="concat('[', $w, ',', $s, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $e, ',', $s, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $e, ',', $n, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $w, ',', $n, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $w, ',', $s, ']')"/>
                  <xsl:text>]]}</xsl:text>
                </geom>
                
                <location><xsl:value-of select="concat(
                  (number($s) + number($n)) div 2,
                  ',',
                  (number($w) + number($e)) div 2)"/></location>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
        <!--<xsl:value-of select="($e + $w) div 2"/>,<xsl:value-of select="($n + $s) div 2"/></field>-->
      </xsl:for-each>
    </xsl:for-each>
      
      
    <xsl:for-each select=".//gmd:temporalElement/*/gmd:extent/gml:TimePeriod">
      <xsl:variable name="start"
        select="gml:beginPosition|gml:begin/gml:TimeInstant/gml:timePosition"/>
      <xsl:variable name="end"
        select="gml:endPosition|gml:end/gml:TimeInstant/gml:timePosition"/>
      
      <xsl:variable name="zuluStartDate"
        select="$start"/>
      <xsl:variable name="zuluEndDate"
        select="$end"/>
      
      <xsl:if test="$zuluStartDate != '' and $zuluEndDate != ''">
        <resourceTemporalDateRange>
          <gte><xsl:value-of select="$zuluStartDate"/></gte>       
          <xsl:if test="$start &lt; $end and not($end/@indeterminatePosition = 'now')">
            <lte><xsl:value-of select="$zuluEndDate"/></lte>        
          </xsl:if>
        </resourceTemporalDateRange>
        <resourceTemporalExtentDateRange>
          <gte><xsl:value-of select="$zuluStartDate"/></gte>
          <xsl:if test="$start &lt; $end and not($end/@indeterminatePosition = 'now')">
            <lte><xsl:value-of select="$zuluEndDate"/></lte>
          </xsl:if>
        </resourceTemporalExtentDateRange>
      </xsl:if>
      <xsl:if test="$start &gt; $end">
        <indexingErrorMsg>Warning / Field resourceTemporalDateRange /
          Lower range bound '<xsl:value-of select="."/>' can not be
          greater than upper bound '<xsl:value-of select="$end"/>'.
          Date range not indexed.</indexingErrorMsg>
      </xsl:if>
    </xsl:for-each>
    
    <xsl:for-each select=".//gmd:verticalElement/*">
      <xsl:variable name="min"
        select="gmd:minimumValue/*/text()"/>
      <xsl:variable name="max"
        select="gmd:maximumValue/*/text()"/>
      
      <resourceVerticalRange>
        <gte><xsl:value-of select="normalize-space($min)"/></gte>
        <xsl:if test="$min &lt; $max">
          <lte><xsl:value-of select="normalize-space($max)"/></lte> 
        </xsl:if>
      </resourceVerticalRange>
    </xsl:for-each>
    
    <!-- TODO: Service information -->
    
  </xsl:template>
  
  
  <!-- Distribution info -->
  <xsl:template match="gmd:distributionInfo/*" mode="index">
    <xsl:for-each select="gmd:transferOptions/*/
      gmd:onLine/*[gmd:linkage/gmd:URL != '']">
      
      <xsl:variable name="transferGroup"
        select="count(ancestor::gmd:transferOptions/preceding-sibling::gmd:transferOptions)"/>
      <xsl:variable name="protocol"
        select="gmd:protocol/*/text()"/>
      <xsl:variable name="linkName"
        select="gn-fn-index:json-escape((gmd:name/*/text())[1])"/>
      
      <linkUrl>
        <xsl:value-of select="gmd:linkage/gmd:URL"/>
      </linkUrl>
      <linkProtocol>
        <xsl:value-of select="$protocol"/>
      </linkProtocol>
      <xsl:element name="linkUrlProtocol{replace($protocol[1], '[^a-zA-Z0-9]', '')}">
        <xsl:value-of select="gmd:linkage/gmd:URL"/>
      </xsl:element>
      <link>
        <protocol><xsl:value-of select="(gmd:protocol/*/text())[1]"/></protocol>
        <url><xsl:value-of select="gmd:linkage/gmd:URL"/></url>
        <name><xsl:value-of select="$linkName"/></name>
        <description><xsl:value-of select="gmd:description/gco:CharacterString/text()"/></description>
        <function><xsl:value-of select="gmd:function/gmd:CI_OnLineFunctionCode/@codeListValue"/></function>
        <applicationProfile><xsl:value-of select="gmd:applicationProfile/gco:CharacterString/text()"/></applicationProfile>
        <group><xsl:value-of select="$transferGroup"/></group>
      </link>
      
      <!-- TODO: Review -->
      <!--<xsl:if test="$operatesOnSetByProtocol and normalize-space($protocol) != ''">
        <xsl:if test="daobs:contains($protocol, 'wms')">
          <recordOperatedByType>view</recordOperatedByType>
        </xsl:if>
        <xsl:if test="daobs:contains($protocol, 'wfs') or
          daobs:contains($protocol, 'wcs') or
          daobs:contains($protocol, 'download')">
          <recordOperatedByType>download</recordOperatedByType>
        </xsl:if>
      </xsl:if>-->
    </xsl:for-each>
  </xsl:template>
  
  
  <!-- Data quality info -->
  <xsl:template match="gmd:dataQualityInfo/*" mode="index">
    
    <xsl:for-each-group select="gmd:report/*/gmd:result"
      group-by="*/gmd:specification/gmd:CI_Citation/gmd:title/(gco:CharacterString|gmx:Anchor)">
      <xsl:variable name="title" select="current-grouping-key()"/>
      
      <!-- TODO -->
      <!--<xsl:variable name="matchingEUText"
        select="if ($inspireRegulationLaxCheck)
        then daobs:search-in-contains($legalTextList/*, $title)
        else daobs:search-in($legalTextList/*, $title)"/>-->
      
      <xsl:variable name="pass"
        select="*/gmd:pass/gco:Boolean"/>
      
      <!-- TODO -->
      <!--<xsl:if test="count($matchingEUText) = 1">
        <inspireConformResource>
          <xsl:value-of select="$pass"/>
        </inspireConformResource>
      </xsl:if>-->
      
      <xsl:if test="string($title)">
        <specificationConformance>
          <title><xsl:value-of select="$title" /></title>
          <xsl:if test="string(*/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date)">
            <date><xsl:value-of select="*/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date" /></date>  
          </xsl:if>
          <xsl:if test="*/gmd:specification/*/gmd:title/@xlink:href">
            <link><xsl:value-of select="*/gmd:specification/*/gmd:title/@xlink:href"/></link>
          </xsl:if>
          <xsl:if test="*/gmd:explanation/*/text() != ''">
            <explanation><xsl:value-of select="(*/gmd:explanation/*/text())[1]" /></explanation>
          </xsl:if>
          <pass><xsl:value-of select="$pass" /></pass>
        </specificationConformance>
      </xsl:if>
      
      <xsl:element name="conformTo_{replace(normalize-space($title), '[^a-zA-Z0-9]', '')}">
        <xsl:value-of select="$pass"/>
      </xsl:element>
    </xsl:for-each-group>
    
    
    <!-- TODO -->
    <!--<xsl:for-each select="gmd:lineage//gmd:source[@uuidref != '']">
      <xsl:variable name="xlink"
        select="@xlink:href"/>
      
      <hassource><xsl:value-of select="@uuidref"/></hassource>
      <recordLink type="object">{
        "type": "sources",
        "origin": "<xsl:value-of
          select="if ($xlink = '')
          then 'catalog'
          else if ($xlink != '' and
          not(starts-with($xlink, $siteUrl)))
          then 'remote'
          else 'catalog'"/>",
        "to": "<xsl:value-of select="@uuidref"/>",
        "title": "<xsl:value-of select="gn-fn-index:json-escape(@xlink:title)"/>",
        "url": "<xsl:value-of select="$xlink"/>"
        }</recordLink>
    </xsl:for-each>-->
    
    <xsl:copy-of select="gn-fn-index:add-multilingual-field('lineage', gmd:lineage/gmd:LI_Lineage/
      gmd:statement, $languages)"/>
    
    
    <!-- Indexing measure value -->
    <xsl:for-each select="gmd:report/*[
      normalize-space(gmd:nameOfMeasure[0]/gco:CharacterString) != '']">
      <xsl:variable name="measureName"
        select="replace(
        normalize-space(
        gmd:nameOfMeasure[0]/gco:CharacterString), ' ', '-')"/>
      <xsl:for-each select="gmd:result/gmd:DQ_QuantitativeResult/gmd:value">
        <xsl:if test=". != ''">
          <xsl:element name="measure_{replace($measureName, '[^a-zA-Z0-9]', '')}">
            <xsl:value-of select="."/>
          </xsl:element>
        </xsl:if>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>
  
  
  <!-- Metadata index main template -->
  <xsl:template mode="index"
                match="gmd:MD_Metadata">
    <indexingDate>
      <xsl:value-of select="format-dateTime(current-dateTime(), $dateTimeFormat)"/>
    </indexingDate>

    <xsl:variable name="languages" as="node()*">
      <xsl:call-template name="get-languages">
        <xsl:with-param name="metadata" select="."/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:for-each select="$languages[@id = 'default']">
      <mainLanguage>
        <xsl:value-of select="@code"/>
      </mainLanguage>
    </xsl:for-each>
    <xsl:for-each select="$languages[@id != 'default']">
      <otherLanguage>
        <xsl:value-of select="@code"/>
      </otherLanguage>
      <otherLanguageId>
        <xsl:value-of select="@id"/>
      </otherLanguageId>
    </xsl:for-each>
    
    <!-- TODO -->
    <!--<xsl:for-each select="(gmd:dateStamp/*[gn-fn-index:is-isoDate(.)])[1]">
      <dateStamp><xsl:value-of select="date-util:convertToISOZuluDateTime(normalize-space(.))"/></dateStamp>
    </xsl:for-each>-->
    
    
    <!-- Record is dataset if no hierarchyLevel -->
    <xsl:variable name="isDataset" as="xs:boolean"
      select="
      count(gmd:hierarchyLevel[gmd:MD_ScopeCode/@codeListValue='dataset']) > 0 or
      count(gmd:hierarchyLevel) = 0"/>
    
    <xsl:choose>
      <xsl:when test="$isDataset">
        <resourceType>dataset</resourceType>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each select="gmd:hierarchyLevel/*/@codeListValue[normalize-space(.) != '']">
          <resourceType>
            <xsl:value-of select="."/>
          </resourceType>
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
    
    
    <xsl:variable name="recordLinks"
      select="gmd:parentIdentifier/*[text() != '']"/>
    <xsl:choose>
      <xsl:when test="count($recordLinks) > 0">
        <xsl:for-each select="$recordLinks">
          <recordGroup><xsl:value-of select="."/></recordGroup>
          <!-- TODO: Review -->
          <!--<xsl:copy-of select="gn-fn-index:build-record-link(., @xlink:href, @xlink:title, 'parent')"/>-->
          <!--
            TODOES - Need more work with routing -->
          <!--            <recordJoin type="object">{"name": "children", "parent": "<xsl:value-of select="gn-fn-index:json-escape(.)"/>"}</recordLink>-->
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <recordGroup><xsl:value-of select="gmd:fileIdentifier/*[1]"/></recordGroup>
      </xsl:otherwise>
    </xsl:choose>
    
    
    <xsl:apply-templates select="gmd:referenceSystemInfo" mode="index" />
    
    <xsl:apply-templates select="gmd:identificationInfo/*" mode="index">
      <xsl:with-param name="isDataset" select="$isDataset" />
    </xsl:apply-templates>
    
    <xsl:apply-templates select="gmd:distributionInfo/*" mode="index" />

    <xsl:apply-templates select="gmd:dataQualityInfo/*" mode="index" />
      
    <xsl:variable name="record"
                  select="."/>

    <xsl:for-each select="$properties">
      <xsl:variable name="property"
                    select="current()"/>
      <xsl:variable name="values">
        <xsl:evaluate xpath="$property/@xpath"
                      context-item="$record"/>
      </xsl:variable>
      
      
      <xsl:choose>
        <xsl:when test="@type = 'codelist'">        
          <xsl:variable name="mainLanguage" select="$languages[@id = 'default']/@code" />
          
          <otherProperties>
          <xsl:for-each-group select="$values/*" group-by="local-name()">
          
            <xsl:message>group: <xsl:value-of select="local-name()" /></xsl:message>
            
            <entry>
             
              <key>
                <xsl:choose>
                  <!-- TODO : Handle characterSet and resourceCharacterSet -->
                  <xsl:when test="current-grouping-key() = 'topicCategory'">cl_topic</xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="concat('cl_', current-grouping-key())" />
                  </xsl:otherwise>
                </xsl:choose>
              </key>
                
              <value>
              <xsl:for-each select="current-group()">
              
                    <xsl:variable name="codelistName" select="name(*[@codeListValue])"/>
                    <xsl:variable name="codelistLocalName" select="current-grouping-key()"/>
                    
                   
                  
                      
                      <xsl:choose>
                        <xsl:when test="current-grouping-key()='topicCategory'">
                            <xsl:variable name="value" select="*"/>
                            
                            
                          <wrapped xsi:type="codelist" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                              <properties>
                                <entry>
                                  <key>key</key>
                                  <value><xsl:value-of select="$value"/></value>
                                </entry>
                                
                                <xsl:for-each select="$languages">
                                  <xsl:variable name="codelist" select="document(concat('./codelist/',@code, '/codelists.xml'))"/>
                                  <xsl:variable name="translation" select="$codelist/codelists/codelist[@name='gmd:MD_TopicCategoryCode']/entry[code=$value]/label" />
                                  
                                  <xsl:choose>
                                    <xsl:when test="@id='default'">     
                                      <entry>
                                        <key>default</key>
                                        <value><xsl:value-of select="$translation"/></value>
                                      </entry>      
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <entry>
                                        <key><xsl:value-of select="concat('lang', @code)" /></key>
                                        <value><xsl:value-of select="$translation"/></value>
                                      </entry>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:for-each>
                              </properties>
                            </wrapped>
                            
                          </xsl:when>
                          
                          <xsl:otherwise>
                            <xsl:variable name="value" select="*[@codeListValue]"/>
                            <wrapped xsi:type="codelist" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                              <properties>
                                <entry>
                                  <key>key</key>
                                  <value><xsl:value-of select="$value/@codeListValue"/></value>
                                </entry>
                                
                                
                                <xsl:for-each select="$languages">
                                  <xsl:variable name="codelist" select="document(concat('./codelist/',@code, '/codelists.xml'))"/>
                                  <xsl:variable name="translation" select="$codelist/codelists/codelist[@name=$codelistName]/entry[code=$value]/label" />
                                  
                                  <xsl:choose>
                                    <xsl:when test="@id='default'">     
                                      <entry>
                                        <key>default</key>
                                        <value><xsl:value-of select="$translation"/></value>
                                      </entry>      
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <entry>
                                        <key><xsl:value-of select="concat('lang', @code)" /></key>
                                        <value><xsl:value-of select="$translation"/></value>
                                      </entry>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:for-each>
                              </properties>
                            </wrapped>
                            
                          </xsl:otherwise>
                        </xsl:choose>                   
                                 
              
              </xsl:for-each>     
                  
              </value>
            </entry>
            
        </xsl:for-each-group>
          </otherProperties>
        </xsl:when>
        <xsl:when test="@type = 'integer'">
          <xsl:for-each select="$values/*">
            <xsl:variable name="value" select="."/>
            
            <xsl:element name="{$property/@name}">
              <xsl:value-of select="$value"/>
            </xsl:element>
          </xsl:for-each>
        
        </xsl:when>
        <xsl:when test="@type = 'date'">
          <xsl:for-each select="$values/*">
            <xsl:variable name="value" select="."/>
            
            <xsl:element name="{$property/@name}">
              <xsl:value-of select="normalize-space($value)"/>
            </xsl:element>
          </xsl:for-each>
          
        </xsl:when>
        <xsl:when test="@type = 'contact'">
          <xsl:apply-templates mode="index"
                               select="$values">
            <xsl:with-param name="elementName" select="$property/@name" />
          </xsl:apply-templates>
        </xsl:when>
        <xsl:when test="@type = 'link'"></xsl:when>
        <xsl:when test="@type = 'multilingual'">
          <xsl:for-each select="$values/*">
            <xsl:variable name="value" select="."/>

            <xsl:element name="{$property/@name}">
              <xsl:for-each select="$languages">
                <xsl:variable name="id" select="@id" /> 
                <entry>
                  <key>
                    <xsl:value-of select="@id"/>
                  </key>
                  <value>
                    <xsl:choose>
                      <xsl:when test="@id='default'"><xsl:value-of select="normalize-space($value/(gco:CharacterString|gmx:Anchor)/text())"/></xsl:when>
                      <xsl:otherwise><xsl:value-of select="normalize-space($value/gmd:PT_FreeText/gmd:textGroup/gmd:LocalisedCharacterString[@locale = concat('#', $id)]/text())"/></xsl:otherwise>
                    </xsl:choose>
                  </value>
                </entry>
              </xsl:for-each>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$values castable as xs:int
                            or $values castable as xs:string">
              <xsl:element name="{$property/@name}">
                <xsl:value-of select="normalize-space($values)"/>
              </xsl:element>
            </xsl:when>
            <xsl:otherwise>
              <xsl:for-each select="$values/*">
                <xsl:element name="{$property/@name}">
                  <xsl:value-of select="normalize-space(current()/*/text())"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:otherwise>
            <!--<xsl:when test="$values instance of xs:int">
            </xsl:when>-->
          </xsl:choose>

        </xsl:otherwise>
      </xsl:choose>

    </xsl:for-each>
  </xsl:template>


  <xsl:function name="gn-fn-index:json-escape" as="xs:string?">
    <xsl:param name="v" as="xs:string?"/>
    <xsl:choose>
      <xsl:when test="normalize-space($v) = ''"></xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="replace(replace(replace(replace(replace($v,
          '\\','\\\\'),
          $doubleQuote, $escapedDoubleQuote),
          '&#09;', '\\t'),
          '&#10;', '\\n'),
          '&#13;', '\\r')"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  
  
  <xsl:function name="gn-fn-index:add-multilingual-field" as="node()*">
    <xsl:param name="fieldName" as="xs:string"/>
    <xsl:param name="elements" as="node()*"/>
    <xsl:param name="languages" as="node()?"/>
    <xsl:copy-of select="gn-fn-index:add-multilingual-field($fieldName, $elements, $languages, false())"/>
  </xsl:function>
  
  <xsl:function name="gn-fn-index:add-multilingual-field" as="node()*">
    <xsl:param name="fieldName" as="xs:string"/>
    <xsl:param name="elements" as="node()*"/>
    <xsl:param name="languages" as="node()?"/>
    <xsl:param name="asJson" as="xs:boolean?"/>
    
    <xsl:message>multilingual-field: <xsl:value-of select="$fieldName" /></xsl:message>
    <!--<xsl:message>multilingual-field elements: <xsl:copy-of select="$elements" /></xsl:message>-->
    
    <xsl:message>multilingual-field elements count: <xsl:value-of select="count($elements[not(@xml:lang)])" /></xsl:message>
    <xsl:message>multilingual-field elements languages: <xsl:copy-of select=" $languages" /></xsl:message>
   
    <xsl:variable name="mainLanguage"
      select="$languages[@id='default']/@code"/>
    
    <!--    <xsl:message>gn-fn-index:add-field <xsl:value-of select="$fieldName"/></xsl:message>-->
    <!--    <xsl:message>gn-fn-index:add-field languages <xsl:copy-of select="$languages"/></xsl:message>-->
    
    <xsl:variable name="isArray"
      select="count($elements[not(@xml:lang)]) > 1"/>
    <xsl:for-each select="$elements">
      <xsl:variable name="element" select="."/>
      <xsl:variable name="textObject" as="node()*">
        <xsl:choose>
          <xsl:when test="$languages">
            <xsl:message>multilingual-field elements languages 1</xsl:message>
            <!-- The default language -->
            <entry>
              
         
            <xsl:for-each select="$element//(*:CharacterString|*:Anchor)[. != '']">
              <default><xsl:value-of select="."/></default> 
            </xsl:for-each>
            
            <xsl:for-each select="$element//*:LocalisedCharacterString[. != '']">
              <xsl:variable name="elementLanguage"
                select="replace(@locale, '#', '')"/>
              <xsl:variable name="elementLanguage3LetterCode"
                select="$languages[@id = $elementLanguage]/@code"/>
              
              <xsl:if test="$elementLanguage3LetterCode != ''">
                <xsl:variable name="field"
                  select="concat('lang', $elementLanguage3LetterCode)"/>
                <xsl:element name="{$field}">
                  <xsl:value-of select="."/>
                </xsl:element>
              </xsl:if>
            </xsl:for-each>
            </entry>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message>multilingual-field elements languages 2</xsl:message>
            <!-- Index each values in a field. -->
            <xsl:for-each select="distinct-values($element[. != ''])">
              <default><xsl:value-of select="."/></default> 
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose>
        
        <xsl:for-each select="$element//*:Anchor/@xlink:href">
          <link><xsl:value-of select="."/></link>
        </xsl:for-each>
      </xsl:variable>
      
      <xsl:if test="$textObject != ''">
        <xsl:element name="{$fieldName}">
          <xsl:copy-of select="$textObject"/>
        </xsl:element>
      </xsl:if>
    </xsl:for-each>
  </xsl:function>
  
  
  <xsl:template name="build-thesaurus-fields">
    <xsl:param name="thesaurus" as="xs:string"/>
    <xsl:param name="thesaurusId" as="xs:string"/>
    <xsl:param name="keywords" as="node()*"/>
    <xsl:param name="mainLanguage" as="xs:string?"/>
    <xsl:param name="allLanguages" as="node()?"/>
    
    <!-- Index keyword characterString including multilingual ones
     and element like gmx:Anchor including the href attribute
     which may contains keyword identifier. -->
    <xsl:variable name="thesaurusField"
      select="concat('th_', replace($thesaurus, '[^a-zA-Z0-9\-_]', ''))"/>
    
    <xsl:element name="{$thesaurusField}Number">
      <xsl:value-of select="count($keywords)"/>
    </xsl:element>
    
    <xsl:if test="count($keywords) > 0">
      <xsl:element name="{$thesaurusField}">
        <xsl:attribute name="type" select="'object'"/>
        [<xsl:for-each select="$keywords">
          <!-- TODO -->
          <xsl:variable name="uri" select="$thesaurusId" />
          <!--<xsl:variable name="uri"
            select="util:getKeywordUri((*/text())[1], $thesaurusId, $mainLanguage)"/>-->
          
          <xsl:variable name="k">
            <xsl:choose>
              <xsl:when test="$uri != ''">
                <!-- Add an anchor -->
                <xsl:copy>
                  <gmx:Anchor xlink:href="{$uri}"></gmx:Anchor>
                  <xsl:copy-of select="*"/>
                </xsl:copy>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="."/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          
          <xsl:value-of select="gn-fn-index:add-multilingual-field('keyword', $k, $allLanguages)/text()"/>
          <xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>]
      </xsl:element>
      
      
      <!-- If keyword is related to a thesaurus available
      in current catalogue, checked the keyword exists in the thesaurus.
      If not, report an error in indexingErrorMsg field.
      
      This case may trigger editor warning message when a keyword is not
       found in the thesaurus. Try to anticipate this and advertise those
       records in the admin.
       
       TODO: Thesaurus id must be defined by a check in thesaurus manager based on multilingual titles.-->
      
      <!-- TODO -->
      <!--<xsl:for-each select="$keywords">
        <xsl:if test="$thesaurusId != ''
          and util:getKeywordUri((*/text())[1], $thesaurusId, $mainLanguage) = ''">
          <indexingErrorMsg>Warning / Keyword <xsl:value-of select="(*/text())[1]"/> not found in <xsl:value-of select="$thesaurusId"/>.</indexingErrorMsg>
          <indexingError>true</indexingError>
        </xsl:if>
      </xsl:for-each>-->
      
      
      <xsl:variable name="thesaurusTree" as="node()">
        <values>
          <!--<xsl:for-each select="$keywords">
            <xsl:variable name="nodes" as="node()*">
              <xsl:copy-of select="*:CharacterString/text()
                |*:Anchor/text()
                |*:Anchor/@xlink:href"/>
              <xsl:if test="not(*:Anchor)">
                &lt;!&ndash; TODO &ndash;&gt;
                <xsl:variable name="uri" select="$thesaurusId"/>
                &lt;!&ndash;<xsl:variable name="uri"
                  select="util:getKeywordUri((*/text())[1], $thesaurusId, $mainLanguage)"/>&ndash;&gt;
                <xsl:if test="$uri != ''">
                  <xsl:attribute name="xlink:href" select="$uri"/>
                </xsl:if>
              </xsl:if>
            </xsl:variable>
            <xsl:for-each select="$nodes">
              <xsl:variable name="keywordTree" as="node()*">
                <xsl:call-template name="get-keyword-tree-values">
                  <xsl:with-param name="keyword"
                    select="."/>
                  <xsl:with-param name="thesaurus"
                    select="$thesaurusId"/>
                  <xsl:with-param name="language"
                    select="$mainLanguage"/>
                </xsl:call-template>
              </xsl:variable>
              
              <xsl:variable name="type"
                select="if (name() = 'xlink:href') then 'key' else 'default'"/>
              <xsl:for-each select="$keywordTree[. != '']">
                <xsl:element name="{$type}">
                  <xsl:value-of select="concat($doubleQuote, gn-fn-index:json-escape(.), $doubleQuote)"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:for-each>
          </xsl:for-each>-->
        </values>
      </xsl:variable>
      
      
      <xsl:if test="count($thesaurusTree/*) > 0">
        <xsl:element name="{$thesaurusField}_tree">
          <xsl:attribute name="type" select="'object'"/>{
          <xsl:variable name="defaults"
            select="distinct-values($thesaurusTree/default)"/>
          <xsl:variable name="keys"
            select="distinct-values($thesaurusTree/key)"/>
          
          <xsl:if test="count($defaults) > 0">"default": [
            <xsl:for-each select="$defaults">
              <xsl:sort select="."/>
              <xsl:value-of select="."/><xsl:if test="position() != last()">,</xsl:if>
            </xsl:for-each>
            ]<xsl:if test="count($keys) > 0">,</xsl:if>
          </xsl:if>
          <xsl:if test="count($keys) > 0">"key": [
            <xsl:for-each select="$keys">
              <xsl:sort select="."/>
              <xsl:value-of select="."/><xsl:if test="position() != last()">,</xsl:if>
            </xsl:for-each>
            ]
          </xsl:if>
          }</xsl:element>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  
  <!--<xsl:template name="get-keyword-tree-values" as="node()*">
    <xsl:param name="keyword" as="xs:string"/>
    <xsl:param name="thesaurus" as="xs:string"/>
    <xsl:param name="language" as="xs:string?" select="'eng'"/>
    
    <xsl:variable name="paths" as="node()*">
      <xsl:variable name="keywordsWithHierarchy"
        select="util:getKeywordHierarchy(
        normalize-space($keyword), $thesaurus, $language)"/>
      
      <xsl:for-each select="$keywordsWithHierarchy">
        <xsl:variable name="path" select="tokenize(., '\^')"/>
        <xsl:for-each select="$path">
          <xsl:variable name="position"
            select="position()"/>
          <value><xsl:value-of select="string-join($path[position() &lt;= $position], '^')"/></value>
        </xsl:for-each>
      </xsl:for-each>
    </xsl:variable>
    
    <xsl:copy-of select="$paths"/>
  </xsl:template>-->
</xsl:stylesheet>
