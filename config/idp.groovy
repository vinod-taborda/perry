import groovy.json.JsonSlurper

def parse = { text ->
    if (text == null || "" == text) {
        return null
    }
    new JsonSlurper().parseText(text);
}
universalUserToken.userId = idpToken["safid.racfid"]?.toUpperCase()

universalUserToken.roles = parse(parse(idpToken.basicprofile)?.User_Properties?.Roles)?.Selections?.keySet() as HashSet
if(!universalUserToken.roles) {
    universalUserToken.roles = new HashSet<>()
    println "INFO: There are no IDP roles provided"
}
