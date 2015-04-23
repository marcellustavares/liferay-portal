'use strict';

// Karma configuration

var basePath = process.cwd() + '/../../../../';

var jsPath = 'portal-web/docroot/html/js/';

var defaultConfig = {
	// base path that will be used to resolve all patterns (eg. files, exclude)
	basePath: basePath,

	// browsers: ['Chrome', 'Firefox', 'IE9 - Win7', 'IE10 - Win7', 'IE11 - Win7'],
	browsers: ['Chrome'],

	// frameworks to use
	// available frameworks: https://npmjs.org/browse/keyword/karma-adapter
	frameworks: ['chai', 'mocha', 'sinon'],

	// list of files to exclude
	exclude: [],

	// preprocess matching files before serving them to the browser
	// available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
	preprocessors: {
		'portal-web/docroot/html/js/liferay/*.js': ['coverage'],
		'modules/apps/dynamic-data-lists/dynamic-data-lists-web/src/META-INF/resources/js/*.js': ['coverage']
	},
	// test results reporter to use
	// possible values: 'dots', 'progress'
	// available reporters: https://npmjs.org/browse/keyword/karma-reporter
	reporters: ['coverage', 'progress'],

	coverageReporter: {
		dir: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/coverage',
	},

	// web server port
	port: 9876,

	// enable / disable colors in the output (reporters and logs)
	colors: true,

	// level of logging
	// possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
	logLevel: 'info',

	// enable / disable watching file and executing tests whenever any file changes
	autoWatch: false,

	// Continuous Integration mode
	// if true, Karma captures browsers, runs the tests and exits
	singleRun: true
};

// list of files / patterns to load in the browser
defaultConfig.files = [
	'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/mock_base.js'
];

var portalProperties = basePath + 'portal-impl/src/portal.properties';

var properties = require('./tests/properties.js');

properties.read(portalProperties, function(data) {
	var props = data[0];

	var bareboneFiles = props['javascript.barebone.files'].split(',');

	bareboneFiles.forEach(
		function(file) {
			defaultConfig.files.push(
				{
					pattern: jsPath + file,
					included: true,
					served: true
				}
			);

			if (file === 'liferay/modules.js') {
				defaultConfig.files.push('modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/mock_modules.js');
			}
		}
	);

	defaultConfig.files = defaultConfig.files.concat(
		[
			'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/mock_available_languages.js',
			'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/mock_language.js',

			{
				pattern: jsPath + 'aui/**/*.css',
				included: false,
				served: true
			},

			{
				pattern: jsPath + 'aui/**/*.js',
				included: false,
				served: true
			},

			{
				pattern: jsPath + 'liferay/*.js',
				included: false,
				served: true,
				watched: true
			},

			{
				pattern: 'modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-checkbox/src/META-INF/resources/*.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-radio/src/META-INF/resources/*.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-select/src/META-INF/resources/*.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-text/src/META-INF/resources/*.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/src/META-INF/resources/js/*.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/src/META-INF/resources/third-party/*.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/mock_portlet_modules.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/*/assets/*',
				included: false,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/form_test_base.js',
				included: true,
				served: true
			},

			{
				pattern: 'modules/apps/dynamic-data-lists/dynamic-data-lists-web/tests/*/*-test.js',
				included: true,
				served: true,
				watched: true
			}
		]
	);

	module.exports = function(config) {
		config.set(defaultConfig);
	};
});