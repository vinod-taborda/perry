import groovy.json.JsonSlurper

def parse = { text ->
    if (text == null || "" == text) {
        return null
    }
    new JsonSlurper().parseText(text);
}

counter = 0
while (counter <= idpToken.UserAttributes.size()) {
    if(idpToken.UserAttributes[counter].Name?.toUpperCase().equals("CUSTOM:RACFID")) {
		universalUserToken.userId = idpToken.UserAttributes[counter].Value?.toUpperCase()
		break;    
    }
    counter++
}

//universalUserToken.roles = parse(parse(idpToken.basicprofile)?.User_Properties?.Roles)?.Selections?.keySet() as HashSet
//if(!universalUserToken.roles) {
//    universalUserToken.roles = new HashSet<>()
//    println "INFO: There are no IDP roles provided"
//}
