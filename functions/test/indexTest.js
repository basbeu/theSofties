/**
* deeply inspired by
* https://github.com/firebase/functions-samples/blob/master/quickstarts/uppercase/functions/test/test.offline.js
*/

const chai = require('chai');
const assert = chai.assert;
const sinon = require('sinon');

const admin = require('firebase-admin');
const test = require('firebase-functions-test')();

describe('Cloud Functions', () => {
  let myFunctions;
  let adminInitStub;

  before(() => {
	  	// Here we stub admin.initializeApp to be a dummy function that doesn't do anything.
	    adminInitStub = sinon.stub(admin, 'initializeApp');
		// Now we can require index.js and save the exports inside a namespace called myFunctions.
		myFunctions = require('../index');
	});	

  after(() => {
    // Restore admin.initializeApp() to its original method.
    adminInitStub.restore();
    // Do other cleanup tasks.
    test.cleanup();
  });

describe('sendNotification', () => {

	let oldDatabase;
    before(() => {
      // Save the old database method so it can be restored after the test.
      oldDatabase = admin.firestore;
    });

    after(() => {
      // Restoring admin.database() to the original method.
      admin.firestore = oldDatabase;
    });

	it('should send a push notification when the notifications collection of a user changes', () => {
      
      // Stubs are objects that fake and/or record function calls.
      // These are excellent for verifying that functions have been called and to validate the
      // parameters passed to those functions.

	    const firebaseStub = sinon.stub();
	    const collectionStub = sinon.stub();
	    const docStub = sinon.stub();
	    const getStub = sinon.stub();
	    const sendToDeviceStub = sinon.stub();

      	const collection = "users";
		const tokenId = "token007";
		const userId = "007";
		const notificationId = "notif007";

		const snap = {
			"data": () => {
				message: "Unit test notification"
			}
		};

		const payload = {
			notification: {
				title: "New Notification",
				body: "Unit test notification",
				icon: "default"
			}
		};

		const user = {
			_fieldsProto: {
				token_id: {
					stringValue: tokenId
				}
			}
		};

		const params = {
			user_id: userId,
			notification_id: notificationId
		};

		//data for sending to sendNotification function, corresponds to first parameter of function
		Object.defineProperty(admin, 'firestore', { get: () => firebaseStub });
		firebaseStub.returns({ collection: collectionStub });
		collectionStub.withArgs(collection).returns({doc: docStub });
		docStub.withArgs(userId).returns({ "get": () => Promise.resolve(user)});

		admin['messaging'] = () => {
			return {
				sendToDevice: sendToDeviceStub
			}
		}

		sendToDeviceStub.withArgs(tokenId, payload).returns(Promise.resolve(true));

    	//wrap the sendNotification function since it is a non-HTTP function
		const wrapped = test.wrap(myFunctions.sendNotification);
		console.log(snap);
		assert.equal(wrapped((snap, { params: params })), true);

    });
});
});