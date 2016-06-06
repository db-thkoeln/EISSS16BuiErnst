function register(app, db) {
	app.post("/station", require("./post-station")(app, db));
	app.get("/station/:id", require("./get-station")(app, db));
	app.put("/station/:id", require("./change-station")(app, db));
	app.delete("/station/:id", require("./delete-station")(app, db));
	app.get("/station", require("./get-all-station")(app, db));
}
module.exports = {
	register: register
};