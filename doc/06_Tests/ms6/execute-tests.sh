for f in `ls test/unit/helpers/`; do echo -e "\n\n>>> $f" >> unit_tests_helpers.results; ruby -I test test/unit/helpers/$f -v >> unit_tests_helpers.results; done

for f in `ls test/functional/`; do echo -e "\n\n>>> $f" >> functional_tests.results; ruby -I test test/functional/$f -v >> functional_tests.results; done

for f in `ls test/unit/`; do echo -e "\n\n>>> $f" >> unit_tests.results; ruby -I test test/unit/$f -v >> unit_tests.results; done
