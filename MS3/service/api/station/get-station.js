function getStation(app, db) {
	return function(req, res) {
		
		db.get("station:" + req.params.id, function(err, rep) {
			if (rep) {
				res.type("json").send(rep);
			}
			else {
				res.status(404).type("text").send("Die Station mit der ID " + req.params.id + " wurde nicht gefunden");
			}
		});
	};
}
module.exports = getStation;