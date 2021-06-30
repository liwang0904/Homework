import pandas as pd
from matplotlib import pyplot as plt

df = pd.read_csv("/Users/rxx/Desktop/Homework/Research/Outcome_2_Result_b_KM_graph.csv", skiprows=9)
#print(df['Cohort 1: Survival Probability'].head(5))

x = df['Time (Days)']

cohort1 = df['Cohort 1: Survival Probability']
lower1 = df['Cohort 1: Survival Probability 95 % CI Lower']
upper1 = df['Cohort 1: Survival Probability 95 % CI Upper']

cohort2 = df['Cohort 2: Survival Probability']
lower2 = df['Cohort 2: Survival Probability 95 % CI Lower']
upper2 = df['Cohort 2: Survival Probability 95 % CI Upper']

plt.plot(x, 1 - cohort1, '-', label="Cohort 1")
plt.fill_between(x, 1 - lower1, 1 - upper1, alpha=0.2)

plt.plot(x, 1 - cohort2, '-', label="Cohort 2")
plt.fill_between(x, 1 - lower2, 1 - upper2, alpha=0.2)

plt.legend()
plt.xlabel("Time (Days)")
plt.ylabel("Hazard Probability")
plt.show()
