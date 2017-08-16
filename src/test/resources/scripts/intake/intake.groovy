def counties = [:]
def getCounty = {code ->
    if(counties.code == null) {
        counties.code = [code: code, privileges: [], units: []]
    }
    return counties.code
}

def getUnit = { code, id ->
    def county = getCounty(code)
    def unit = county.units.find {it.id == id}
    if(unit == null) {
        unit = [id:id, authorities:[]]
        county.units.push(unit)
    }
    return unit
}
authorization.authorityPrivilege.findAll {
    it.authPrivilegeCode == "P" && it.endDate == null

} each {
    getCounty(it.countyCode).privileges.push(it.authPrivilegeTypeDesc)
}

authorization.unitAuthority.findAll {
   it.endDate == null
}.each {
    getUnit(it.countyCode, it.assignedUnit).authorities.push(it.unitAuthorityCodeDesc)
}

def supervisorAuthorities = ["S", "A", "T", "B"]

def supervisor = authorization.unitAuthority != null && authorization.unitAuthority.size() > 0 &&  authorization.unitAuthority.every {a -> supervisorAuthorities.any {it == a.unitAuthorityCode}}


[user: authorization.userId, roles: [supervisor? "Supervisor" : "SocialWorker" ], staffId: authorization.staffPersonId, counites : counties.values() ]

