function getMeasuredPlant(app, db) {
	return function(req, res) {
		db.get('uu:'+req.params.uid+':measuredPlant:'+req.params.id, function(err, rep){
			if(rep) {
				res.type('json').send(rep);
			}
			else {
				res.status(404).type('text').send('Die Pflanze Serie mit der ID '+req.params.id+' wurde nicht gefunden');
			}
		});
	};
}
module.exports = getMeasuredPlant;