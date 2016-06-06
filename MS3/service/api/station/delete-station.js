function deleteStation(app, db) {
	return function(req, res) {
		db.del("station:" + req.params.id, function(err, rep) {
			if (rep == 1) {
				res.status(200).type("text").send("OK");
			}
			else {
				res.status(404).type("text").send("Die Station mit der ID " + req.params.id + " wurde nicht gefunden");
			}
		});
	};
}
module.exports = deleteStation;