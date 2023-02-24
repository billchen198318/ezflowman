findFormOIDsOfProcess 有可能帶回多筆 oid 所以取最後一筆

例如回傳多筆, 如下
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <soapenv:Body>
      <ns1:findFormOIDsOfProcessResponse soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns1="http://webservice.nana.dsc.com/">
         <findFormOIDsOfProcessReturn xsi:type="xsd:string">0af5253cefaf100484d196fddccb88ca,bc09f160efb2100484d196fddccb88ca</findFormOIDsOfProcessReturn>
      </ns1:findFormOIDsOfProcessResponse>
   </soapenv:Body>
</soapenv:Envelope>

