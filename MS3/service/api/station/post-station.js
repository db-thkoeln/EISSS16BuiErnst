function postStation(app, db) {
	return function(req, res) {
		var newStation = req.body;
		db.incr("id:posts", function(err, rep) {
			newStation.id = rep;
			db.set("station:" + newStation.id, JSON.stringify(newStation), function(err, rep) {
				res.json(newStation);
			});
		});
	};
}
module.exports = postStation;