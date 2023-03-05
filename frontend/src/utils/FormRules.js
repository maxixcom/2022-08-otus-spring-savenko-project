const ruleRequired = function() {
  return (value) => !!value || 'Поле обязательно должно быть заполнено'
}

const ruleEmail = function() {
  return (value) => (value && value.indexOf('@') > 0) || 'Email должен быть валидным адресом'
}

const ruleNotEmptyCollection = function() {
  return (value) => (value && value.length > 0) || 'Должно быть указано хотя бы одно значение'
}

const ruleFileMaxSize = function (maxSize, maxSizeString) {
  return (value) => !value || value.size < maxSize || 'Размер файла не должен превышать ' + maxSizeString
}

const ruleFileImage = function () {
  const types = ['image/jpeg', 'image/png', 'image/gif']

  return (value) => !value || types.includes(value.type) || 'Картинка должна быть любого типа из JPEG,PNG,GIF'
}

export { ruleRequired, ruleEmail, ruleNotEmptyCollection, ruleFileMaxSize, ruleFileImage }
