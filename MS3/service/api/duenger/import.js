function register(app, db) {
	app.post("/duenger", require("./post-duenger")(app, db));
	app.get("/duenger/:id", require("./get-duenger")(app, db));
	app.put("/duenger/:id", require("./change-duenger")(app, db));
	app.delete("/duenger/:id", require("./delete-duenger")(app, db));
	app.get("/duenger", require("./get-all-duenger")(app, db));
}
module.exports = {
	register: register
};