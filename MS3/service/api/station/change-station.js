function changeStation(app, db) {
	return function(req, res) {
		db.get("station:" + req.params.id, function (err, rep) {
			var json = JSON.parse(rep);
			for (var key in req.body) {
				json[key] = req.body[key];
			}
			db.set("station:" + req.params.id, JSON.stringify(json), function (err, rep) {
				res.json(json);
			});
		});
	}
}
module.exports = changeStation;