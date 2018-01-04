import gov.ca.cwds.rest.api.domain.auth.GovernmentEntityType

def authorization = user.authorization
if (authorization == null) {
    return [user : user.userId,
            roles: user.roles]
}
privileges = []
authorization.authorityPrivilege.findAll {
    it.authPrivilegeCode == "P" && it.endDate == null

} each {
    privileges.push it.authPrivilegeTypeDesc
}

def supervisorAuthorities = ["S", "A", "T", "B"]

def supervisor = authorization.unitAuthority != null && authorization.unitAuthority.size() > 0 && authorization.unitAuthority.every { a ->
    supervisorAuthorities.any {
        it == a.unitAuthorityCode
    }
}


[user       : authorization.userId,
 roles      : user.roles + [supervisor ? "Supervisor" : "SocialWorker"],
 staffId    : authorization.staffPersonId,
 county_name: authorization.county,
 county_code: authorization.staffPerson.countyCode,
 county_cws_code: GovernmentEntityType.findByCountyCd(authorization.staffPerson.countyCode).sysId,
 privileges : privileges]

