function getDuenger(app, db) {
	return function(req, res) {
		
		db.get("duenger:" + req.params.id, function(err, rep) {
			if (rep) {
				res.type("json").send(rep);
			}
			else {
				res.status(404).type("text").send("Der Duenger mit der ID " + req.params.id + " wurde nicht gefunden");
			}
		});
	};
}
module.exports = getDuenger;