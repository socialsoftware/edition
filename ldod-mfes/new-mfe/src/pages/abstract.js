/** @format */
function* testYield(value) {
	while (true) {
		let step = yield value++;
		if (step) {
			value += step;
		}
	}
}
const generatorFunc = testYield(1);
console.log(generatorFunc.next().value);
console.log(generatorFunc.next().value);
console.log(generatorFunc.next().value);
