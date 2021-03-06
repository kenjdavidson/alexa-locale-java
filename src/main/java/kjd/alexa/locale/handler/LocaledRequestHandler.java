package kjd.alexa.locale.handler;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import lombok.extern.log4j.Log4j;

/**
 * Base {@link RequestHandler} used to provided {@link Locale} based {@link ResourceBundle}
 * customization in {@link Response}(s).  {@link ResourceBundle}(s) are not required, but default
 * values should be provided, as no {@link MissingResourceException} is thrown.
 * <p> 
 * {@link ResourceBundle}(s) require a resource base (filename prefix) when attempting to load, this
 * can be configured by using the {@code @ResourceBundleBase} annotation.  Without the
 * annotation, the full class name will be used (requiring the locale file to be within the same
 * class path.
 * <p>
 * A default {@link Log4j} logger is available with a default setting of WARN.  The logger 
 * settings can be overwritten by providing a custom properties file and using the 
 * environment variable {@code -Dlog4j.configuration=<config-file>} as described in the Log4j
 * documentation.
 * <p>
 * An basic extending class only needs to implement the {@link #handleRequest(HandlerInput, ResourceBundle)}
 * method:
 * <pre><code>
 * 	protected Optional<Response> handleRequest(HandlerInput input, Locale locale) {
 * 		String speech = getMessage(locale, 
 * 			"welcome", 
 * 			"Welcome to the Alexa locale skill.");
 * 		return input.getResponseBuilder()
 * 			.withSpeech(speech)
 * 			.withReprompt(speech)
 * 			.withSimpleCard("Hello", speech)
 * 			.build();
 * 	}
 * </code></pre>
 * 
 * @author kendavidson
 *
 */
public abstract class LocaledRequestHandler extends LocaledHandler
		implements RequestHandler {

	/**
	 * Handles the request by loading the {@link ResourceBundle} using the specified
	 * base file and then calling {@link #handleRequest(HandlerInput, ResourceBundle)}.
	 * 
	 * @param request
	 * @return 
	 */
	@Override
	public Optional<Response> handle(HandlerInput input) {
		String requestId = input.getRequestEnvelope().getRequest().getRequestId();	
		
		String[] localeStrings = input.getRequestEnvelope().getRequest().getLocale().split("-");		
		getLogger().debug(String.format("Handling input request %s with RequestEnvelope: %s", 
				requestId, input.getRequestEnvelope().toString()));
		
		Locale locale = new Locale(localeStrings[0], localeStrings[1]);
		getLogger().debug(String.format("Handling input request %s with Locale %s",
				requestId, locale.toString())); 
				
		return handleRequest(input, locale);
	}
	
	/**
	 * Perform the Request handling with a provided {@link ResourceBundle}.  The {@link ResourceBundle}
	 * can be {@code null}, in this case, the {@link RequestHandler} should provide a default
	 * {@link Response}.
	 * 
	 * @param intput
	 * @param rb
	 * @return
	 */
	protected abstract Optional<Response> handleRequest(HandlerInput input, Locale locale);	
	
}
