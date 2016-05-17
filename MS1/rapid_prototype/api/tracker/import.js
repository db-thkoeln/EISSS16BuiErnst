function register(app, db) {
	app.post("/tracker", require("./post-tracker")(app, db));
	app.get("/tracker/:id", require("./get-tracker")(app, db));
	app.put("/tracker/:id", require("./change-tracker")(app, db));
	app.delete("/tracker/:id", require("./delete-tracker")(app, db));
	app.get("/tracker", require("./get-all-tracker")(app, db));
}
module.exports = {
	register: register
};