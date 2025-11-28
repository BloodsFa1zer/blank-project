<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gems="http://www.diamondfund.ua/gems"
                exclude-result-prefixes="gems">
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/">
        <GemsByPreciousness xmlns="http://www.diamondfund.ua/gems">
            <!-- Precious gems -->
            <PreciousGems>
                <xsl:apply-templates select="//gems:gem[gems:preciousness='precious']"/>
            </PreciousGems>
            
            <!-- Semi-precious gems -->
            <SemiPreciousGems>
                <xsl:apply-templates select="//gems:gem[gems:preciousness='semi-precious']"/>
            </SemiPreciousGems>
        </GemsByPreciousness>
    </xsl:template>

    <xsl:template match="gems:gem">
        <gem>
            <xsl:copy-of select="@*"/>
            <xsl:copy-of select="*"/>
        </gem>
    </xsl:template>

</xsl:stylesheet>

