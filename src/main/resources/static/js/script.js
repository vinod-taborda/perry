if (window.location.toString().indexOf("error") !== -1) {
  document.getElementById("error").innerHTML = "Invalid username or password!";
}

function validateLoginString() {
  var loginInfo = document.getElementById("username");
  var loginError = document.getElementById("login-error");
  var submitBtn = document.getElementById("submitBtn");
  if (isValidJson(loginInfo.value)) {
    submitBtn.disabled = false;
    while (loginError.firstChild) {
      loginError.removeChild(loginError.firstChild);
    }

  } else {
    submitBtn.disabled = true;
    loginError.innerHTML = "Please enter valid authorization JSON. \n";
    loginError.innerHTML += "Example: {\n"
        + "\t\"user\": \"RACFID\",\n"
        + "\t\"staffId\": \"0X5\",\n"
        + "\t\"roles\": [\n"
        + "\t\t\"Supervisor\"\n"
        + "\t],\n"
        + "\t\"county_code\": \"56\",\n"
        + "\t\"county_name\": \"Ventura\",\n"
        + "\t\"privileges\": [\n"
        + "\t\t\"Countywide Read\",\n"
        + "\t\t\"Sensitive Persons\"\n"
        + "\t]\n"
        + "}";
  }
}

function isValidJson(json) {
  try {
    JSON.parse(json);
    return true;
  } catch (e) {
    return false;
  }
}