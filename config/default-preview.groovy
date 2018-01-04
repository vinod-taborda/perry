import gov.ca.cwds.rest.api.domain.auth.GovernmentEntityType

[
 user       : user.userId,
 roles      : user.roles,
 staffId    : user.getParameter("staffId"),
 county_name: user.getParameter("county_name"),
 county_code: user.getParameter("county_code"),
 county_cws_code: GovernmentEntityType.findByCountyCd(user.getParameter("county_code")).sysId,
 privileges : user.getParameter("privileges")
]