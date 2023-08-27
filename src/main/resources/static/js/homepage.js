function getCookie(name) {
  const cookies = document.cookie.split("; ");
  for (const cookie of cookies) {
    const [cookieName, cookieValue] = cookie.split("=");
    if (cookieName === name) {
      return cookieValue;
    }
  }
  return null;
}


const user = getCookie("loggedInUser");
if(user && !(user === "null")) {
	const e = document.getElementById("text");
	e.innerText = user;
} else {
	window.location.href = "/";
}

const logout_btn = document.getElementById("logout_btn");
logout_btn.addEventListener("click", ()=>{
	const userResponse = confirm("Do you want to Logout?");
	if(userResponse) {
		var cookieName = 'loggedInUser';
		document.cookie = `${cookieName}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
		window.location.href = "/";
	}
})