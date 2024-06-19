import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white', //background color
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    color: 'black', //text color
    padding: 10,
    width: '80%',
     //placeholder text color
  },
  placeholderStyle: {
    backgroundColor: 'lightgray',
    color: 'blue',
  }
});

export default styles;
