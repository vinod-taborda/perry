import groovy.json.JsonSlurper

universalUserToken.userId = idpToken["safid.racfid"]?.toUpperCase()
def parser = new JsonSlurper()
try {
    universalUserToken.roles = parser.parseText(parser.parseText(idpToken.basicprofile).User_Properties.Roles).Selections.keySet() as HashSet
}catch (all) {
    all.printStackTrace()
}