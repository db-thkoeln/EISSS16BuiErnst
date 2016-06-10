function register(app, db) {
	app.post("/user", require("./post-user")(app, db));
	app.get("/user/:id", require("./get-user")(app, db));
	app.put("/user/:id", require("./change-user")(app, db));
	app.delete("/user/:id", require("./delete-user")(app, db));
	app.get("/user", require("./get-all-user")(app, db));
	app.post("/user/:uid/measuredPlant", require("./post-measuredPlant")(app, db));
	app.get("/user/:uid/measuredPlant/:id", require("./get-measuredPlant")(app, db));
	app.put("/user/:uid/measuredPlant/:id", require("./change-measuredPlant")(app, db));
	app.delete("/user/:uid/measuredPlant/:id", require("./delete-measuredPlant")(app, db));
	app.get("/user/:uid/measuredPlant", require("./get-all-measuredPlant")(app, db));
}
module.exports = {
	register: register
};