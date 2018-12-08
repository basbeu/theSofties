'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.firestore.document(`users/{user_id}/notifications/{notification_id}`)
					.onCreate((snap, context) => {
						const user_id = context.params.user_id;
						const notification_id = context.params.notification_id;

						const to_data = admin.firestore().collection("users").doc(user_id).get();

						return Promise.all([to_data]).then(result => {
							const token_id = result[0]._fieldsProto.token_id.stringValue;

							console.log(result[0]._fieldsProto.token_id);
							console.log("Sending to token_id: " + token_id);

							if(token_id) {

								const payload = {
									notification: {
										title: "New Notification",
										body: snap.data().message,
										icon: "default"
									}
								};

								return admin.messaging().sendToDevice(token_id, payload).then(result => {
									console.log("Notification sent.");
								});
							} else {
								return console.log('No token_id for user_id: ' + user_id);
							}
						});
					});