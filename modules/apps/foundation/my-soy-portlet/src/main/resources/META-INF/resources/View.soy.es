{namespace Templates.View}

/**
 * Prints My Soy Portlet main view.
 *
 * @param userName
 * @param link
 */
{template .render}
	<h1>List View</h1>

	Welcome {$userName}! <a href="{$link}">Click here to navigate to another view.</a>

	<br />

	<button id="button">Dropdown</button>

	{call Templates.Popover.render}
		{param content: 'Hello' /}
		{param id: 'popover' /}
		{param selector: '#button' /}
		{param title: 'Hola' /}
		{param visible: false /}
	{/call}
{/template}