function getParking(app, db) {
	return function(req, res) {
		
		db.get("parking:" + req.params.id, function(err, rep) {
			if (rep) {
				res.type("json").send(rep);
			}
			else {
				res.status(404).type("text").send("Der Parkplatz mit der ID " + req.params.id + " wurde nicht gefunden");
			}
		});
	};
}
module.exports = getParking;