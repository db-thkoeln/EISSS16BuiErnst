function postPlant(app, db) {
	return function(req, res) {
		var newPlant = req.body;
		db.incr("id:ss", function(err, rep) {
			newPlant.id = rep;
			db.set("plant:" + newPlant.id, JSON.stringify(newPlant), function(err, rep) {
				res.json(newPlant);
			});
		});
	};
}
module.exports = postPlant;