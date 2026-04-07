fetch('http://localhost:8080/api/user')
       .then(response => response.json)
       .then(data =>
       console.log(data));