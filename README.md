https://github.com/AlexKniss/A6	



&nbsp;	With the manual UI testing, I had to divide what was recorded into separate tests. While I did have to re-structure the recorded code into separate test methods, but all the assertions were already done for me. I found that the tests didn't take as long to run compared to the ones the LLM wrote.

&nbsp;	The LLM had some strange behavior in writing its tests. It ended up making several extra files in order to bug fix itself. I think the project folder could get quite cluttered. A cause of a lot of failing tests was that the LLM struggled to click the cart button. It figured it out, but made a timeout function that hit every time before correctly getting to the cart.

&nbsp;	While I didn't have to write a whole lot to start the LLM writing tests, which was a few sentences followed by the direct instructions, the LLM had to try many, many times to recorrect itself. I had to stop it in the middle of its tracks to request it to remove the timeout function. There were a few times the chat window suggested to me to stop and write another prompt since the LLM was iterating. Another strange thing was that although I had the Playwright MCP installed and enabled, it tried making the tests without using it. I noticed it used it at first, but didn't consistently use it. I was too far into this until I realized it had done a very large amount using the MCP and just resorted to guesswork. I've disabled tests 5-7 for this reason.

