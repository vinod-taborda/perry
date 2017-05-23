statewideReadPriv = authorization.authorityPrivilege.find({it.authPrivilegeTypeDesc == "Statewide Read"}) //1486
sensitivePriv = authorization.authorityPrivilege.find({it.authPrivilegeTypeDesc == "Sensitive Persons"}) //1482
sealedPriv = authorization.authorityPrivilege.find({it.authPrivilegeTypeDesc == "Sealed"}) //1481
def role = "none"
if(statewideReadPriv != null && sealedPriv != null && sensitivePriv != null) {
    role = "all"
}
else if(statewideReadPriv != null && sensitivePriv != null){
    role = "sensitive"
}
else if (statewideReadPriv != null && sealedPriv != null) {
    role = "sealed"
}

[user: authorization.userId, roles: [role]]

//code = P
//endDate = null