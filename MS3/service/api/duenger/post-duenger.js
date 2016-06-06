function postDuenger(app, db) {
	return function(req, res) {
		var newDuenger = req.body;
		db.incr("id:postd", function(err, rep) {
			newDuenger.id = rep;
			db.set("duenger:" + newDuenger.id, JSON.stringify(newDuenger), function(err, rep) {
				res.json(newDuenger);
			});
		});
	};
}
module.exports = postDuenger;