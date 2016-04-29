function register(app, db) {
	app.post("/plant", require("./post-plant")(app, db));
	app.get("/plant/:id", require("./get-plant")(app, db));
	app.put("/plant/:id", require("./change-plant")(app, db));
	app.delete("/plant/:id", require("./delete-plant")(app, db));
	app.get("/plant", require("./get-all-plant")(app, db));
}
module.exports = {
	register: register
};