universalUserToken.userId = idpToken["user"]
universalUserToken.roles = idpToken["roles"]
universalUserToken.setParameter("staffId", idpToken["staffId"])
universalUserToken.setParameter("county_name", idpToken["county_name"])
universalUserToken.setParameter("county_code", idpToken["county_code"])
universalUserToken.setParameter("privileges", idpToken["privileges"])