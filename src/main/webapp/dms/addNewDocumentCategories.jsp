<%@ page
	import="java.util.*, oscar.dms.data.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
HashMap<String,String> doctypeerrors = new HashMap<String,String>();
if (request.getAttribute("doctypeerrors") != null) {
	doctypeerrors = (HashMap<String,String>) request.getAttribute("doctypeerrors");
}
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<script> 

function submitUpload(object) {
    object.Submit.disabled = true;
    
    return true;
}
</script>
<title>Add New Document Type</title>
</head>
<body>
<div>
<% Iterator iter = doctypeerrors.keySet().iterator();
while (iter.hasNext()){%>
<font class="warning">Error: <bean:message
	key="<%= doctypeerrors.get(iter.next())%>" /></font><br />
<% } %> 
</div>

<table class="MainTable" id="scrollNumber1" name="documentCategoryTable" style="margin: 0px;">
<tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Document Types</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Add New Document Type</td>
                        </tr>
                    </table>
                </td>
            </tr>
<html:form action="/dms/addDocumentType" method="POST"
	enctype="multipart/form-data" styleClass="forms"
	onsubmit="return submitUpload(this)">
<table>
	<tr>
		<td><b>Select module name: </b></td>
	
		<td >
			<input <% if (doctypeerrors.containsKey("modulemissing")) {%>
				class="warning" <%}%> id="function" type="radio" name="function" value="Demographic"> Demographic</td>
		<td >
			<input <% if (doctypeerrors.containsKey("modulemissing")) {%>
				class="warning" <%}%> id="function" type="radio" name="function" value="Provider"> Provider</td>
	
	</tr>
	
	<tr>
	<td><b>Enter document Type: </b></td>
	<td >
		<input <% if (doctypeerrors.containsKey("doctypemissing")) {%>
				class="warning" <%}%> id="docType" type="text" name="docType" value=""> <br></td>
	</tr>
	
	

	<tr>
		<td><input type="submit" name="submit" value="Submit"/> </td> 
		<td><input type="button" name="button" value="Cancel" onclick=self.close()> </td>
	</tr>
</table>
</html:form>
</table>
</body>
</html>