function postMeasuredPlant(app, db) {
	return function(req, res) {
		var newMeasuredPlant = req.body;

		db.incr('uzaehler:'+req.params.uid+':measuredPlant', function(err, rep){
		newMeasuredPlant.id = rep;

			db.set('uu:'+req.params.uid+':measuredPlant:'+newMeasuredPlant.id, JSON.stringify(newMeasuredPlant), function(err, rep){
				res.json(newMeasuredPlant);
			});
		});
	};
}
module.exports = postMeasuredPlant;