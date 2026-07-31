<?xml version="1.0" encoding="UTF-8"?>
<WebElementEntity>
   <description>Calendar date button by text - Generic locator for clicking specific date number in visible calendar</description>
   <name>calendar_date_button</name>
   <tag></tag>
   <elementGuidId>audit-calendar-date-btn-001</elementGuidId>
   <selectorCollection>
      <entry>
         <key>XPATH</key>
         <value>//button[contains(@class, 'day') and not(contains(@class, 'day-outside')) and normalize-space(.)='${dateNumber}']</value>
      </entry>
      <entry>
         <key>CSS</key>
         <value></value>
      </entry>
   </selectorCollection>
   <selectorMethod>XPATH</selectorMethod>
   <useRalativeImagePath>false</useRalativeImagePath>
   <variableLinks>
      <TestObjectProperty>
         <name>xpath</name>
         <type>TESTOBJECT_PROPERTY</type>
         <value>//button[contains(@class, 'day') and not(contains(@class, 'day-outside')) and normalize-space(.)='${dateNumber}']</value>
      </TestObjectProperty>
   </variableLinks>
</WebElementEntity>
