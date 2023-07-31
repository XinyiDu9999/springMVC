<form id="myForm" action= "/user/login" method="POST" accept-charset="UTF-8">
  <input type="text" name="username">
  <input type="password" name="password">
  <button type="submit">Submit</button>
</form>

<script>
  document.getElementById("myForm").addEventListener("submit", function(event) {
    event.preventDefault(); // 阻止表单默认提交行为

    var formData = {
      username: this.username.value,
      password: this.password.value
    };

    var jsonData = JSON.stringify(formData);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", this.action, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(jsonData);
  });
</script>
