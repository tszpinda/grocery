# grocery

[![wercker status](https://app.wercker.com/status/db78d475e3bd8f5d6cb36169b53919d9/m "wercker status")](https://app.wercker.com/project/bykey/db78d475e3bd8f5d6cb36169b53919d9)

## An example app using:
- dropwizard
- hibernate
- spring
- heroku


## Contains:
- rest api
- dao tests
- resource tests
- integration tests

# heroku
You can see running app on heroku:
### admin pages
- https://tsz-grocery.herokuapp.com/admin/

### available rest apis
- GET: https://tsz-grocery.herokuapp.com/fruits
- GET: https://tsz-grocery.herokuapp.com/fruits/search
- @POST: https://tsz-grocery.herokuapp.com/fruits/{id}/price
- @PUT: https://tsz-grocery.herokuapp.com/fruits/addOrOverride
